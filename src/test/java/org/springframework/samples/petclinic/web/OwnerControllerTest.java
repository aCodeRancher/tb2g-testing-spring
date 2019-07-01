package org.springframework.samples.petclinic.web;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.samples.petclinic.model.Owner;
import org.springframework.samples.petclinic.service.ClinicService;
import org.springframework.test.context.junit.jupiter.web.SpringJUnitWebConfig;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.ArrayList;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.reset;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringJUnitWebConfig(locations = {"classpath:spring/mvc-test-config.xml", "classpath:spring/mvc-core-config.xml"})
class OwnerControllerTest {

    @Autowired
    OwnerController ownerController;

    @Autowired
    ClinicService clinicService;

    MockMvc mockMvc;

    @Captor
    ArgumentCaptor<String> stringArgumentCaptor;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(ownerController).build();
    }

    @AfterEach
    void tearDown(){
        reset(clinicService);
    }
    @Test
    void testFindByNameNotFound() throws Exception {
        mockMvc.perform(get("/owners")
                    .param("lastName", "Dont find ME!"))
                .andExpect(status().isOk())
                .andExpect(view().name("owners/findOwners"));
    }

    @Test
    void testFindByName() throws Exception{
        Owner owner = new Owner();
        owner.setId(1);
        owner.setLastName("Smith");
        Collection<Owner> results = new ArrayList<Owner>();
        results.add(owner);
        given(clinicService.findOwnerByLastName("Smith")).willReturn(results);
        mockMvc.perform(get("/owners")
                .param("lastName", "Smith"))
                .andExpect(status().is3xxRedirection())
                .andExpect(view().name("redirect:/owners/1"));
        then(clinicService).should().findOwnerByLastName(stringArgumentCaptor.capture());
        assertThat(stringArgumentCaptor.getValue()).isEqualTo("Smith");
    }

     @Test
     void testFindByLastNameList() throws Exception{
         Owner owner = new Owner();
         owner.setId(1);
         owner.setLastName("Smith");
         Owner owner1 = new Owner();
         owner1.setId(2);
         owner1.setLastName("Smith");
         Owner owner2 = new Owner();
         owner2.setId(3);
         owner2.setLastName("Franklin");
         Collection<Owner> results = new ArrayList<Owner>();
         results.add(owner);
         results.add(owner1);
         results.add(owner2);
         given(clinicService.findOwnerByLastName("Smith")).willReturn(results);
         assertThat(results.size()==2);
         mockMvc.perform(get("/owners")
                 .param("lastName", "Smith"))
                 .andExpect(status().isOk())
                 .andExpect(model().attributeExists("selections"))
                 .andExpect(view().name("owners/ownersList"));
         then(clinicService).should().findOwnerByLastName(stringArgumentCaptor.capture());
         assertThat(stringArgumentCaptor.getValue()).isEqualTo("Smith");

     }
    @Test
    void initCreationFormTest() throws Exception {
        mockMvc.perform(get("/owners/new"))
            .andExpect(status().isOk())
            .andExpect(model().attributeExists("owner"))
            .andExpect(view().name("owners/createOrUpdateOwnerForm"));
    }
}