/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author Chip
 */
public enum FxmlView
{    
    LOGIN
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/LoginView.fxml";
        }
    },
    
    ROOT
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/RootView.fxml";
        }
    },
    
    MAIN
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/MainView.fxml";
        }
    },

    CUSTOMER
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/CustomerView.fxml";
        }
    },

    REMINDER
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/ReminderView.fxml";
        }
    },
            
    SCHEDULE
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/ScheduleView.fxml";
        }
    },    
    
    REPORTS
    {
        @Override
        public String getFxmlFile()
        {
            return "/Controller_View/ReportView.fxml";
        }
    };
    
    abstract public String getFxmlFile();
    
}
