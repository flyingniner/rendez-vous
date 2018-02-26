/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

import java.time.DayOfWeek;
import java.time.LocalDate;

/**
 *
 * @author chip
 */
public class AppointmentFilter
{
//    private long currentMonthPosition = 0;
//    private long currentWeekPosition = 0;
    private LocalDate selectedDate;
    private boolean byMonth = false;
    private boolean byWeek = false;
    
    public AppointmentFilter() {}
    public AppointmentFilter(boolean byMonthSelected, boolean byWeekSelected, LocalDate selectedDate)
    {
//        this.currentMonthPosition = currentMonthPosition;
//        this.currentWeekPosition = currentWeekPosition;
        this.selectedDate = selectedDate.minusDays(selectedDate.getDayOfWeek().compareTo(DayOfWeek.MONDAY));
        this.byMonth = byMonthSelected;
        this.byWeek = byWeekSelected;
    }
    
//    public long getCurrentMonthPosition() { return this.currentMonthPosition; }
    public void setCurrentMonthPosition() { this.selectedDate = LocalDate.now();}
//    
//    public long getCurrentWeekPosition() { return this.currentWeekPosition; }
    public void setCurrentWeekPosition() { this.selectedDate = LocalDate.now(); }
    
    public boolean isWeeklySelected() { return this.byWeek; }
    public void setByWeek(boolean isSelected) { this.byWeek = isSelected; }
    
    public boolean isMonthlySelected() { return this.byMonth; }
    public void setByMonth(boolean isSelected) { this.byMonth = isSelected; }
    
    public LocalDate getCurrentDatePosition() { return this.selectedDate; }
    public void setCurentDatePosition(LocalDate date) { this.selectedDate = date; }
    
    public void changeMonthlyPosition(long changeValue) 
    {   
        selectedDate = selectedDate.plusMonths(changeValue);
    }

    public void changeWeeklyPosition(long changeValue)
    {        
        selectedDate = selectedDate.plusWeeks(changeValue);            
    }
    
    
}