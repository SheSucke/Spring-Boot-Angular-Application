/*
 * EventControllerTest
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package eu.profinit.stm.controller.event;

import eu.profinit.stm.SportTeamManagerApplicationTest;
import eu.profinit.stm.dto.event.EventDto;
import eu.profinit.stm.exception.EntityAlreadyExistsException;
import eu.profinit.stm.exception.EntityNotFoundException;
import eu.profinit.stm.exception.UserIsAlreadyInEventException;
import eu.profinit.stm.mapper.EventMapper;
import eu.profinit.stm.model.event.Event;
import eu.profinit.stm.model.event.Place;
import eu.profinit.stm.service.event.EventService;
import eu.profinit.stm.service.invitation.InvitationService;
import eu.profinit.stm.model.user.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import java.io.StringWriter;
import java.time.LocalDateTime;
import java.util.ArrayList;

/**
 * Test Event controller.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SportTeamManagerApplicationTest.class)
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
@WebAppConfiguration
@AutoConfigureMockMvc
@ActiveProfiles({"stub_repository","stub_services","webTest","authentication","test"})
public class EventControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EventService eventService;
    @Autowired
    private InvitationService invitationService;

    private Event event;

    /**
     * Creates Event data transfer object for comparing with request results.
     */
    @Before
    public void setUp() throws EntityNotFoundException, UserIsAlreadyInEventException, EntityAlreadyExistsException {
        Place place = new Place("Profinit","Tychonova 2", 1L);
        User loggedUser = new User("Ivan", "Stastny", "$2a$10$ruiQYEnc3bXdhWuCC/q.E.D.1MFk2thcPO/fVrAuFDuugjm3XuLZ2", "is@gmail.com");
        event = new Event(LocalDateTime.now(), 5, false, place, loggedUser, new ArrayList<>(), new ArrayList<>());
        event.setEntityId(0L);
        eventService.createNewEvent(EventMapper.toDto(event));
        eventService.addNewMessage("is@gmail.com","Testuji",0L);
        invitationService.createNewInvitation("is@gmail.com",0L);
    }

    /**
     * Testing findEventById, positive endind
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void findEventByIdFindsEventById() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventDto.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(EventMapper.toDto(eventService.findEventById(0L)), sw);
        String eventXml = sw.toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/0")).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().string(eventXml));
    }

    /**
     * Testing findEventById for non-existing event, returns HttpStatus.NOT_FOUND
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void findEventByIdForNonExistingIdReturnsNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/1")).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Testing creation of a new Event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void createNewEventCreatesNewEvent() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventDto.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        jaxbMarshaller.marshal(EventMapper.toDto(event), sw);
        String eventXml = sw.toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event")
                                .contentType(MediaType.APPLICATION_XML)
                                .content(eventXml)).
        andExpect(MockMvcResultMatchers.status().isCreated()).
                andExpect(MockMvcResultMatchers.content().xml(eventXml));
    }

    /**
     * Testing update of an existing Event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void updateEventUpdatesEvent() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventDto.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        event.setIsCanceled(true);
        jaxbMarshaller.marshal(EventMapper.toDto(event), sw);
        String eventXml = sw.toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0")
                                .contentType(MediaType.APPLICATION_XML)
                                .content(eventXml)).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andExpect(MockMvcResultMatchers.content().xml(eventXml));
    }

    /**
     * Testing update of a non-existing Event, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void updateNonExistingEventThrowsHTTPStatusNotFound() throws Exception {
        JAXBContext jaxbContext = JAXBContext.newInstance(EventDto.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        StringWriter sw = new StringWriter();
        event.setCapacity(9);
        jaxbMarshaller.marshal(EventMapper.toDto(event), sw);
        String eventXml = sw.toString();

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1")
                                .contentType(MediaType.APPLICATION_XML)
                                .content(eventXml)).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Testing changing status of an existing Event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void changeEventStatusChangesEventStatus() throws Exception {
        MvcResult returnValue = mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/status")
                                .contentType(MediaType.APPLICATION_XML)).
                andExpect(MockMvcResultMatchers.status().isOk()).
                andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("<isCanceled>true</isCanceled>"));
    }

    /**
     * Testing changing status of a non-existing Event, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void changeEventStatusForNonExistingEventReturnNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1/status")
                                .contentType(MediaType.APPLICATION_XML)).
                andExpect(MockMvcResultMatchers.status().isNotFound()).
                andExpect((MockMvcResultMatchers.status().reason("Event entity not found!")));
    }

    /**
     * Testing getAllMessages gets all messages for and existing event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getAllMessagesGetsAllMessages() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/0/messages")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("Testuji"));
    }

    /**
     * Testing getAllMessages gets all messages for a non-existing event, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getAllMessagesForNonExistingEventReturnNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/1/messages")).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Testing adding a new message with a logged user and existing event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    @WithUserDetails("is@gmail.com")
    public void addNewMessageAddsNewMessage() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/message/Ahoj!")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        System.out.println(result);
        Assert.assertTrue(result.contains("Ahoj!"));
        Assert.assertTrue(result.contains("Ivan"));
        Assert.assertTrue(result.contains("Stastny"));
        Assert.assertTrue(result.contains("is@gmail.com"));

    }

    /**
     * Testing adding a new message with a logged user and non-existing event, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    @WithUserDetails("is@gmail.com")
    public void addNewMessageToAndNonExistentEventReturnHttpStatusNotFound() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1/message/Ahoj!")).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Testing getAllInvitation gets all invitations for an existing event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getAllInvitationsGetsAllInvitations() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/0/invitations")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        System.out.println(result);

        Assert.assertTrue(result.contains("\"name\":\"Ivan\""));
        Assert.assertTrue(result.contains("\"surname\":\"Stastny\""));
        Assert.assertTrue(result.contains("\"status\":\"PENDING\""));
    }

    /**
     * Testing getAllInvitation returns HttpStatus.NOT_FOUND for a non-existing event
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getAllInvitationsForNonExistingEventReturnNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/1/invitations")).
                andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    /**
     * Testing addNewInvitation adds new invitation for an existing event and user, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void AddNewInvitationAddsNewInvitation() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/is@email.cz")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("PENDING"));
        Assert.assertTrue(result.contains("Jirka"));
        Assert.assertTrue(result.contains("Vesely"));
        Assert.assertTrue(result.contains("is@email.cz"));
    }

    /**
     * Testing addNewInvitation adds new invitation for a non-existing event, existing user and first invite, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void AddNewInvitationForNonExistingEventReturnNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1/invitation/is@gmail.com")).
                andExpect(MockMvcResultMatchers.status().isNotFound()).
                andExpect((MockMvcResultMatchers.status().reason("Event entity not found!")));
    }

    /**
     * Testing addNewInvitation adds new invitation for an existing event and non-existing user, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void AddNewInvitationForNonExistingUserReturnNotFoundHTTPStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/is@gml.com")).
                andExpect(MockMvcResultMatchers.status().isNotFound()).
                andExpect((MockMvcResultMatchers.status().reason("User entity not found!")));
    }

    /**
     * Testing addNewInvitation adds new invitation for a non-existing event, existing user and second invite, HttpStatus.Conflict expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void addNewInvitationTwiceThrowsUserAlreadyInvitedException() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/dvakrat@gmail.com")).
                andExpect(MockMvcResultMatchers.status().isConflict()).
                andExpect((MockMvcResultMatchers.status().reason(new EntityAlreadyExistsException("User").getMessage())));

    }

    /**
     * Testing changeStatus changes status of an invitation for an existing event, logged user and correct StatusEnum,positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    @WithUserDetails("is@gmail.com")
    public void changeInvitationStatusChangesInvitationStatus() throws Exception {
        MvcResult returnValue = mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/statusChange/ACCEPTED")
                                .contentType(MediaType.APPLICATION_XML)).
                                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("Ivan"));
        Assert.assertTrue(result.contains("Stastny"));
        Assert.assertTrue(result.contains("ACCEPTED"));
    }

    /**
     * Testing changeStatus changes status of an invitation for a non-existing event, logged user and correct StatusEnum, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    @WithUserDetails("is@gmail.com")
    public void changeInvitationStatusForNonExistentEventReturnsNotFoundHttpStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1/invitation/statusChange/ACCEPTED")
                                .contentType(MediaType.APPLICATION_XML)).
                andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    /**
     * Testing changeStatus changes status of an invitation for an existing event, logged user and an incorrect StatusEnum, HttpStatus.BAD_REQUEST expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    @WithUserDetails("is@gmail.com")
    public void changeInvitationStatusForInvalidStatusValueReturnsBadRequestHttpStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/statusChange/NONEXISTENT")).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect((MockMvcResultMatchers.status()
                        .reason("No enum constant eu.profinit.stm.model.invitation.StatusEnum.NONEXISTENT")));
    }

    /**
     * Testing getSortedInvitationList returns list with invitations for a given correct status and an existing event, positive ending
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getSortedInvitationsReturnsSortedInvitations() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/0/invitations/PENDING/sorted")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("Ivan"));
        Assert.assertTrue(result.contains("Stastny"));
        Assert.assertTrue(result.contains("PENDING"));
    }

    /**
     * Testing getSortedInvitationList returns list with invitations for a given correct status and a non-existing event, HttpStatus.NOT_FOUND expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getSortedInvitationsForNonExistingEventReturnNotFoundHttpStatus() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/1/invitations/PENDING/sorted")).
                andExpect(MockMvcResultMatchers.status().isNotFound()).
                andExpect((MockMvcResultMatchers.status().reason("Event entity not found!")));
    }

    /**
     * Testing getSortedInvitationList returns list with invitations for a given incorrect status and existing event, HttpStatus.BAD_REQUEST expected
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getSortedInvitationsForNonExistingStatusReturns() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/event/0/invitations/SENDING/sorted")).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
                andExpect((MockMvcResultMatchers.status()
                        .reason("No enum constant eu.profinit.stm.model.invitation.StatusEnum.SENDING")));
    }

    /**
     * Testing creation of a new guest invitation with an existing event
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void createNewGuestCreatesNewGuest() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/0/invitation/guest/Karel")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("mxPR4fbWzvai60UMLhD3aw=="));
    }

    /**
     * Testing createNewGuestInvitation return NotFound status for non-existent event
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void createNewGuestInvitationReturnsNotFoundForNonExistentEvent() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/event/1/invitation/guest/Karel")).
                andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    /**
     * Testing getGuest invitation for existent event and guest return InvitationDto and OK status.
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getGuestInvitationGetsGuestInvitation() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.get("/guest/invitation/mxPR4fbWzvai60UMLhD3aw==")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("Karel"));
        Assert.assertTrue(result.contains("GUEST"));
        //Added because when you run only this test status is PENDING, when all tests changeStatus test will change status
        //to ACCEPTED
        Assert.assertTrue(result.contains("PENDING") || result.contains("ACCEPTED"));
    }

    /**
     * Testing getGuestInvitation return Bad Request for bad URI
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void getGuestInvitationReturnBadRequestForBadUri() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.get("/guest/invitation/mxPR4fbWzvi60UMLhD3aw==")).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).andReturn();
    }

    /**
     * Testing ChangeGuestInvitationStatus changes status of invitation for existent invitation and valid StatusEnum
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void changeGuestStatusInvitationChangesGuestStatusInvitation() throws Exception {
        MvcResult returnValue =  mockMvc.perform(
                        MockMvcRequestBuilders.post("/guest/invitation/mxPR4fbWzvai60UMLhD3aw==/statusChange/ACCEPTED")).
                andExpect(MockMvcResultMatchers.status().isOk()).andReturn();

        String result = returnValue.getResponse().getContentAsString();

        Assert.assertTrue(result.contains("Karel"));
        Assert.assertTrue(result.contains("GUEST"));
        Assert.assertTrue(result.contains("ACCEPTED"));
    }

    /**
     * Testing changeGuestInvitationStatus return NotFound for non-existent invitation
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void changeGuestStatusInvitationReturnsNotFoundForNonExistentEvent() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/guest/invitation/LIGPFGwdE3HIk4YGdc/9Dg==/statusChange/ACCEPTED")).
                andExpect(MockMvcResultMatchers.status().isNotFound()).andReturn();
    }

    /**
     * Testing changeGuestInvitationStatus return BadRequest fo invalid StatusEnum
     *
     * @throws Exception if request is invalid
     */
    @Test
    public void changeGuestReturnBadRequestForNonExistentStatusEnum() throws Exception {
        mockMvc.perform(
                        MockMvcRequestBuilders.post("/guest/invitation/mxPR4fbWzvai60UMLhD3aw==/statusChange/THROWN")).
                andExpect(MockMvcResultMatchers.status().isBadRequest()).
        andExpect((MockMvcResultMatchers.status()
                .reason("No enum constant eu.profinit.stm.model.invitation.StatusEnum.THROWN")));
    }
}
