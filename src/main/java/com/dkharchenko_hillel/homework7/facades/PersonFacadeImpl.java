package com.dkharchenko_hillel.homework7.facades;

import com.dkharchenko_hillel.homework7.converters.PersonConverter;
import com.dkharchenko_hillel.homework7.dtos.PersonDto;
import com.dkharchenko_hillel.homework7.services.PersonService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.dkharchenko_hillel.homework7.converters.PersonConverter.convertPersonToPersonDto;

@Slf4j
@Component
public class PersonFacadeImpl implements PersonFacade {

    private final PersonService personService;

    public PersonFacadeImpl(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void addPerson(@NonNull PersonDto dto) {
        personService.addPerson(checkName(dto.getFirstName()), checkName(dto.getLastName()),
                checkNumber(dto.getPhoneNumber()), dto.getUsername(), dto.getPassword());
    }

    @Override
    public void removePerson(@NonNull Long id) {
        personService.removePersonById(id);
    }

    @Override
    public PersonDto getPersonByUsername(@NonNull String username) {
        return convertPersonToPersonDto(personService.getPersonByUsername(username));
    }

    @Override
    public List<PersonDto> getAllPersons() {
        return personService.getAllPersons().stream().map(PersonConverter::convertPersonToPersonDto)
                .collect(Collectors.toList());
    }

    @Override
    public void updatePersonFirstNameByUsername(@NonNull String username, @NonNull String firstName) {
        personService.updatePersonFirstNameByUsername(username, checkName(firstName));
    }

    @Override
    public void updatePersonLastNameByUsername(@NonNull String username, @NonNull String lastName) {
        personService.updatePersonLastNameByUsername(username, checkName(lastName));
    }

    @Override
    public void updatePersonPhoneNumberByUsername(@NonNull String username, @NonNull String phoneNumber) {
        personService.updatePersonPhoneNumberByUsername(username, checkNumber(phoneNumber));
    }

    private String checkName(String name) {
        if (name.matches("[A-Za-zА-Яа-я]+")) {
            return name;
        } else {
            log.error("Name is invalid: {}", name);
            throw new IllegalArgumentException("Invalid name");
        }
    }

    private String checkNumber(String number) {
        if (number.matches("[\\d+]+")) {
            return number;
        } else {
            log.error("Number is invalid: {}", number);
            throw new IllegalArgumentException("Invalid number");
        }
    }
}
