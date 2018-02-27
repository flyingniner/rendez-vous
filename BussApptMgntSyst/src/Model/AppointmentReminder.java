/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import static java.util.stream.Collectors.toList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Chip
 */
public class AppointmentReminder
{
    
    public static ObservableList<Appointment> upcommingAppoitments = FXCollections.observableArrayList();
    private int appointmentId;
    private boolean hasBeenReminded;
    
    public static void getUpcomingAppointments(UserClass user)
    {
        try
        {
            ObservableList<Appointment> userAppointments = BussApptMgntSyst.appointments.stream()
                .filter(a -> a.getUserName().equals(user.getUserName()))
                .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));
        
            LocalDateTime now = LocalDateTime.now();
                                                  
            upcommingAppoitments = userAppointments.stream()
                .peek(n -> System.out.println("element started: " + n.appointmentIdProperty()))
                .filter((a -> (now.isBefore(a.getStart())) && 
                       (a.getStart().isBefore(now.plusMinutes(15)))))
                .peek(n -> System.out.println("element filtered: " + n.appointmentIdProperty()))
                .collect(Collectors.collectingAndThen(toList(), l -> FXCollections.observableArrayList(l)));                               
        } 
        catch (Exception e)
        {
            throw e;
        }                
    }
    
}
