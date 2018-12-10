/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package beta;

import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author MainFrame
 */
class portfolioTable {

    private SimpleStringProperty symbol;
    private SimpleStringProperty share;

    portfolioTable(String symbol, String share) {
        //makes the symbol and shares to simple strings
        this.symbol = new SimpleStringProperty(symbol);
        this.share = new SimpleStringProperty(share);

    }

    //accesors
    public SimpleStringProperty getSymbol() {
        return symbol;
    }
    
    public SimpleStringProperty getShare() {
        return share;
    }

    //mutators
    public void setSymbol(SimpleStringProperty symbol) {
        this.symbol = symbol;
    }

    public void setShare(SimpleStringProperty share) {
        this.share = share;
    }

}
