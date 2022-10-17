package com.sblinn.vendingmachine;


import com.sblinn.vendingmachine.controller.VendingMachineController;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;



/**
 *
 * @author sarablinn
 */
public class App {
    
    public static void main(String[] args) {
        
//        UserIO io = new UserIOConsoleImpl();
//        
//        VendingMachineView view = new VendingMachineView(io);
//        
//        VendingMachineDao dao = new VendingMachineDaoFileImpl();
//        
//        VendingMachineAuditDao auditDao = new VendingMachineAuditDaoFileImpl();
//        
//        VendingMachineServiceLayer service = 
//                new VendingMachineServiceLayerImpl(dao, auditDao);
//        
//        VendingMachineController controller = 
//                new VendingMachineController(view, service);
//        
//        controller.run();

        ApplicationContext appContext = new ClassPathXmlApplicationContext(
                "classpath:applicationContext.xml");
        
        VendingMachineController controller = 
                appContext.getBean("controller", VendingMachineController.class);
        
        controller.run();
  
    }
}
