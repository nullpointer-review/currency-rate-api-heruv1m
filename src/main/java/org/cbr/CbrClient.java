package org.cbr;

import com.sun.org.apache.xerces.internal.dom.ElementNSImpl;
import com.sun.org.apache.xerces.internal.dom.TextImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.client.core.SoapActionCallback;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import ru.cbr.web.GetCursOnDateXML;
import ru.cbr.web.GetCursOnDateXMLResponse;

import javax.xml.datatype.XMLGregorianCalendar;
import java.util.List;


public class CbrClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(CbrClient.class);

    public String getRate(String currencyCode, XMLGregorianCalendar date) throws Exception {

        GetCursOnDateXML request = new GetCursOnDateXML();
        request.setOnDate(date);

        GetCursOnDateXMLResponse response = (GetCursOnDateXMLResponse) getWebServiceTemplate()
                .marshalSendAndReceive(
                        "http://www.cbr.ru/DailyInfoWebServ/DailyInfo.asmx",
                        request,
                        new SoapActionCallback("http://web.cbr.ru/GetCursOnDateXML"));

        return parseResponse(currencyCode, response);
    }

    public static String parseResponse(String valuteCh, GetCursOnDateXMLResponse response) throws Exception {

        List<Object> list = response.getGetCursOnDateXMLResult().getContent();
        ElementNSImpl e = (ElementNSImpl) list.get(0);
        NodeList chCodeList = e.getElementsByTagName("VchCode");
        int length = chCodeList.getLength();
        for (int i = 0; i < length; i++) {

            Node valuteChNode = chCodeList.item(i);
            TextImpl textimpl = (TextImpl) valuteChNode.getFirstChild();
            String chVal = textimpl.getData();
            if (chVal.equalsIgnoreCase(valuteCh)) {
                Node parent = valuteChNode.getParentNode();
                NodeList nodeList = parent.getChildNodes();
                int paramLength = nodeList.getLength();

                for (int j = 0; j < paramLength; j++) {
                    Node currentNode = nodeList.item(j);

                    String name = currentNode.getNodeName();
                    Node currentValue = currentNode.getFirstChild();

                    String value = currentValue.getNodeValue();

                    if (name.equalsIgnoreCase("Vcurs")) {
                        return value;
                    }


                }
            }
        }
        return null;

    }


}