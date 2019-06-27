package org.springframework.samples.petclinic.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.PetType;
import org.springframework.samples.petclinic.repository.PetRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


import static org.mockito.BDDMockito.given;
import static  org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class ClinicServiceImplTest {

    @Mock
    private PetRepository petRepository;

    private ClinicService clinicService;

    @BeforeEach
    void setUp(){
        clinicService=  new ClinicServiceImpl(petRepository, null,null, null);
        List<PetType> petTypes = new ArrayList<PetType>();
        PetType petType = new PetType();
        petTypes.add(petType);
        given(petRepository.findPetTypes()).willReturn(petTypes);

    }

    @Test
    @Transactional
    void findPetTypes() {
        Collection<PetType> petTypes = clinicService.findPetTypes();
        then(petRepository).should(times(1)).findPetTypes();
        assertThat(petTypes.size()==1);

    }

}