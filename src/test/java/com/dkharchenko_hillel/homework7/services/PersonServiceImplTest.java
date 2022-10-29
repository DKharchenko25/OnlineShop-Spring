package com.dkharchenko_hillel.homework7.services;

import com.dkharchenko_hillel.homework7.models.Person;
import com.dkharchenko_hillel.homework7.reposiroties.PersonRepository;
import com.dkharchenko_hillel.homework7.services.test_config.PersonServiceImplTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = PersonServiceImplTestConfig.class)
class PersonServiceImplTest {
    @MockBean
    private PersonRepository personRepository;
    @Autowired
    private PersonService personService;

    @Test
    void addPersonSuccess() {
        Person person = new Person();
        person.setFirstName("test");
        person.setLastName("test");
        person.setPhoneNumber("+38093");
        person.setUsername("test");
        person.setPassword("test");
        when(personRepository.save(person)).thenReturn(person);
        personService.addPerson(person.getFirstName(), person.getLastName(), person.getPhoneNumber(),
                person.getUsername(), person.getPassword());
        verify(personRepository, times(1)).save(any(Person.class));
    }

    @Test
    void addPersonMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.addPerson(null, "test",
                "2464", "test", "test"));
        assertThrows(NullPointerException.class, () -> personService.addPerson("test", null,
                "2464", "test", "test"));
        assertThrows(NullPointerException.class, () -> personService.addPerson("test", "test",
                null, "test", "test"));
        assertThrows(NullPointerException.class, () -> personService.addPerson("test", "test",
                "2464", null, "test"));
        assertThrows(NullPointerException.class, () -> personService.addPerson("test", "test",
                "2464", "test", null));
    }

    @Test
    void addPersonMustThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> personService.addPerson("", "test",
                "4565", "test", "test"));
        assertThrows(IllegalArgumentException.class, () -> personService.addPerson("!_??", "test",
                "4565", "test", "test"));
        assertThrows(IllegalArgumentException.class, () -> personService.addPerson("test", "!_??",
                "4565", "test", "test"));
        assertThrows(IllegalArgumentException.class, () -> personService.addPerson("test", "",
                "4565", "test", "test"));
        assertThrows(IllegalArgumentException.class, () -> personService.addPerson("test", "test",
                "test", "test", "test"));
    }


    @Test
    void removePersonByIdSuccess() {
        when(personRepository.existsById(2L)).thenReturn(true);
        personService.removePersonById(2L);
        verify(personRepository, times(1)).deleteById(2L);
    }

    @Test
    void removePersonMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.removePersonById(null));
    }

    @Test
    void removePersonMustThrowIllegalArgumentException() {
        when(personRepository.existsById(1L)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> personService.removePersonById(1L));
    }

    @Test
    void getPersonByUsernameSuccess() {
        Person person = new Person();
        person.setUsername("Tom");
        when(personRepository.findPersonByUsername(person.getUsername())).thenReturn(person);
        assertEquals(person, personService.getPersonByUsername("Tom"));
    }

    @Test
    void getPersonByUsernameMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.getPersonByUsername(null));
    }

    @Test
    void getPersonByUsernameMustThrowIllegalArgumentException() {
        when(personRepository.findPersonByUsername("test")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonByUsername("test"));
    }

    @Test
    void getPersonByIdSuccess() {
        Person person = new Person();
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        assertEquals(person, personService.getPersonById(1L));
    }

    @Test
    void getPersonByIdMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.getPersonById(null));
    }

    @Test
    void getPersonByUIdMustThrowIllegalArgumentException() {
        when(personRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(IllegalArgumentException.class, () -> personService.getPersonById(2L));
    }

    @Test
    void getAllPersonsSuccess() {
        List<Person> personList = new ArrayList<>();
        when(personRepository.findAll()).thenReturn(personList);
        assertEquals(personList, personService.getAllPersons());
    }

    @Test
    void updatePersonFirstNameByUsernameSuccess() {
        Person person = new Person();
        person.setUsername("success");
        when(personRepository.findPersonByUsername("success")).thenReturn(person);
        personService.updatePersonFirstNameByUsername("success", "test");
        verify(personRepository, times(1)).updatePersonFirstNameByUsername("success", "test");
    }

    @Test
    void updatePersonFirstNameMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.updatePersonFirstNameByUsername("test", null));
        assertThrows(NullPointerException.class, () -> personService.updatePersonFirstNameByUsername(null, "test"));
        assertThrows(NullPointerException.class, () -> personService.updatePersonFirstNameByUsername(null, null));
    }

    @Test
    void updatePersonFirstNameMustThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonFirstNameByUsername("test", ""));
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonFirstNameByUsername("test", "!_+?"));
        when(personRepository.findPersonByUsername("test")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonFirstNameByUsername("test", "Tom"));
    }

    @Test
    void updatePersonLastNameByUsernameSuccess() {
        Person person = new Person();
        person.setUsername("success");
        when(personRepository.findPersonByUsername("success")).thenReturn(person);
        personService.updatePersonLastNameByUsername("success", "test");
        verify(personRepository, times(1)).updatePersonLastNameByUsername("success", "test");
    }

    @Test
    void updatePersonLastNameMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.updatePersonLastNameByUsername("test", null));
        assertThrows(NullPointerException.class, () -> personService.updatePersonLastNameByUsername(null, "test"));
        assertThrows(NullPointerException.class, () -> personService.updatePersonLastNameByUsername(null, null));
    }

    @Test
    void updatePersonLastNameMustThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonLastNameByUsername("test", ""));
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonLastNameByUsername("test", "!_+?"));
        when(personRepository.findPersonByUsername("test")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonLastNameByUsername("test", "Tom"));
    }

    @Test
    void updatePersonPhoneNumberByUsernameSuccess() {
        Person person = new Person();
        person.setUsername("success");
        when(personRepository.findPersonByUsername("success")).thenReturn(person);
        personService.updatePersonPhoneNumberByUsername("success", "+380678");
        verify(personRepository, times(1)).updatePersonPhoneNumberByUsername("success", "+380678");
    }

    @Test
    void updatePersonPhoneNumberMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.updatePersonPhoneNumberByUsername("test", null));
        assertThrows(NullPointerException.class, () -> personService.updatePersonPhoneNumberByUsername(null, "+38093"));
        assertThrows(NullPointerException.class, () -> personService.updatePersonPhoneNumberByUsername(null, null));
    }

    @Test
    void updatePersonPhoneNumberMustThrowIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonPhoneNumberByUsername("test", ""));
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonPhoneNumberByUsername("test", "!_+?"));
        when(personRepository.findPersonByUsername("test")).thenReturn(null);
        assertThrows(IllegalArgumentException.class, () -> personService.updatePersonPhoneNumberByUsername("test", "+309875"));
    }

    @Test
    void loadUserByUsernameSuccess() {
        Person person = new Person();
        person.setUsername("success");
        when(personRepository.findPersonByUsername("success")).thenReturn(person);
        assertEquals(person, personService.loadUserByUsername("success"));
    }

    @Test
    void loadUserMustThrowNullPointerException() {
        assertThrows(NullPointerException.class, () -> personService.loadUserByUsername(null));
    }

    @Test
    void loadUserMustThrowUserNotFoundException() {
        when(personRepository.findPersonByUsername("test")).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> personService.loadUserByUsername("test"));
    }
}