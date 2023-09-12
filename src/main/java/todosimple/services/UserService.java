package todosimple.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import todosimple.models.User;
import todosimple.models.enums.ProfileEnum;
import todosimple.repositories.UserRepository;
import todosimple.security.UserSpringSecurity;
import todosimple.services.exceptions.AuthorizationException;
import todosimple.services.exceptions.DataBindingViolationException;
import todosimple.services.exceptions.ObjectNotFoundException;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User findByID(Long id) {

        UserSpringSecurity userSpringSecurity = authenticated();
        if (!Objects.nonNull(userSpringSecurity) || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) && !id.equals(userSpringSecurity.getId())){
            throw new AuthorizationException("Acesso negado");
        }

        Optional<User> user = this.userRepository.findById(id);
        return user.orElseThrow(() -> new ObjectNotFoundException(
                "Usuário não encontrado! Id: " + id + ", Tipo: " + User.class.getName()));
    }

    @Transactional
    public User create(User obj){

        obj.setId(null);
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        obj = this.userRepository.save(obj);
        return obj;
}


    @Transactional
    public User update(User obj){
        User newObj = findByID(obj.getId());
        newObj.setPassword(obj.getPassword());
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findByID(id);
        try{
            this.userRepository.deleteById(id);
        }
        catch (Exception e){
            throw new DataBindingViolationException("não foi possível excluir pois há entidades relacionadas!" + e);
        }
    }

    public static UserSpringSecurity authenticated(){
        try {
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (Exception e){
            return null;
        }
    }
}
