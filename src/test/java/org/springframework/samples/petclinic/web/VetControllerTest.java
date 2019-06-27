package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.samples.petclinic.model.Vet;
import org.springframework.samples.petclinic.model.Vets;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static  org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class VetControllerTest {

    @Mock
    ClinicService clinicService;

    @InjectMocks
    VetController vetController;

    private Collection<Vet> vets;

    @BeforeEach
    void setUp(){
        vets = new ArrayList<>();
        Vet vet = new Vet();
        vets.add(vet);
        given(clinicService.findVets()).willReturn(vets);
    }

    @Test
    void showVetList() {
        Map<String, Object> model = new HashMap<>();
        String output= vetController.showVetList(model);
        assertThat(output.equals("vets/vetList"));
    }

    @Test
    void showResourcesVetList() {
         Vets vets = vetController.showResourcesVetList();
         int listSize = vets.getVetList().size();
         assertThat(listSize==1);
         assertThat(vets).isNotNull();
    }
}