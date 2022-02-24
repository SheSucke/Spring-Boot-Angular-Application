/*
 * MessageMapper
 *
 * 0.1
 *
 * Author: M. Halamka
 */

package cz.profinit.sportTeamManager.mapper;

import cz.profinit.sportTeamManager.dto.event.MessageDto;
import cz.profinit.sportTeamManager.model.event.Message;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@Profile({"test", "Main","stub_services"})
public class MessageMapper {

    /**
     * Map Message class to MessageDto.
     *
     * @param message Message that needs to be mapped.
     * @return MessageDto representing given Message
     */
    public static MessageDto toDto(Message message) {
        return new MessageDto(UserMapper.mapRegisteredUserToRegisteredUserDTO(message.getUser()),message.getText(),message.getDate(), message.getEventId());
    }

    /**
     * Map MessageDto to Message
     *
     * @param messageDto MessageDto that needs to be mapped.
     * @return Message representing given MessageDto
     */
    public static Message toMessage(MessageDto messageDto) {
        return new Message(UserMapper.mapRegisteredUserDTOToRegisteredUser(messageDto.getUser()),messageDto.getText(),messageDto.getDate(), messageDto.getEventId());
    }

    /**
     * Creates list of DTOs from Entity list
     *
     * @param listOfMessages given Entity list
     * @return list of DTOs
     */
    public static List<MessageDto> toListOfDto(List<Message> listOfMessages) {
        List<MessageDto> messageDtoList = new ArrayList<>();

        for (Message message : listOfMessages) {
            messageDtoList.add(MessageMapper.toDto(message));
        }

        return messageDtoList;
    }

}
