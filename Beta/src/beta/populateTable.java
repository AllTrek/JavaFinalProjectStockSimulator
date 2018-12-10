package beta;

import java.io.IOException;
import javafx.beans.property.SimpleStringProperty;

// when using tables to edit conent you have to use single string property 
public class populateTable {

    //initializes user class object
    User currentUser = new User();
    
    //viriables
    private SimpleStringProperty Symbol;
    private SimpleStringProperty Share;
    private SimpleStringProperty price;

    public populateTable(String Symbol, String Share, String price) throws IOException {
        //adds the stock simbol, shares owned and price to table
        this.Symbol = new SimpleStringProperty(Symbol);
        this.Share = new SimpleStringProperty(Share);
        this.price = new SimpleStringProperty(price);
    }

    //accesors
    public String getSymbol() {
        return Symbol.get();
    }

    public String getShare() {
        return Share.get();
    }

    public String getPrice() {
        return price.get();
    }

    //mutators
    public void setSymbol(SimpleStringProperty Symbol) throws IOException {
        this.Symbol = Symbol;
    }

    public void setShare(SimpleStringProperty Share) {
        this.Share = Share;
    }

    public void setPrice(SimpleStringProperty price) throws IOException {
        this.price = price;
    }
}
