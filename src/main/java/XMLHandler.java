import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler {

    String userName;
    String openDate;
    String bankName;
    String passport;

    // сработает, когда парсер натыкается на какой-либо элемент (на начало тега)
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        try {
            if (qName.equals("client") && userName == null){
                userName = attributes.getValue("name");
//                openDate = attributes.getValue("openDate");
//                bankName = attributes.getValue("bank");
                passport = attributes.getValue("passport");
//                DatabaseInteractionService.countClient(userName, openDate, bankName, passport); // отправляем в StringBuilder класса DBConnection
                DatabaseInteractionService.passportInfoUsers(userName, passport); // отправляем в StringBuilder класса DBConnection
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // сработает, когда парсер натыкается на какой-либо элемент (на конец тега)
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("client")) {
            userName = null; // стираем если завершился
        }
    }
}