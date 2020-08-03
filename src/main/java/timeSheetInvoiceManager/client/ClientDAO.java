/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package timeSheetInvoiceManager.client;

import javafx.collections.ObservableList;

/**
 *
 * @author xaboo
 */
public class ClientDAO {

    private ObservableList<Client> clientFXBeans;
    private ClientRepository clientRepository;
    private Client clientBean;
    
    public ClientDAO() {
        init();
    }
 
    private void init() {
        clientFXBeans = (ObservableList<Client>) clientRepository.findAll();
    }
 
    public ObservableList<Client> getClientFXBeans() {
        return clientFXBeans;
    }
}
