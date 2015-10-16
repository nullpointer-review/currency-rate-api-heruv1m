package org.cbr;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;


@RestController
public class RateController {
    private static final Logger log = LoggerFactory.getLogger(RateController.class);
    private static final SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

    @Autowired
    private CbrClient cbrClient;

    @RequestMapping("/rate/{code}/{date}")
    public Object rateWithDate(@PathVariable("code") String code, @PathVariable(value = "date") String date) throws Exception {
        log.info("rateWithDate code [{}], date [{}]", code, date);
        String a = cbrClient.getRate(code, getXMLGregorianCalendarFromDate(date));
        if (a == null) {
            return String.format("Not found currency for code [%s]", code);
        }
        return new RateResponse(code, a, date);
    }

    @RequestMapping("/rate/{code}")
    public Object rate(@PathVariable("code") String code) throws Exception {
        log.info("rate code [{}]", code);
        return rateWithDate(code, formatter.format(new Date()));
    }

    public static XMLGregorianCalendar getXMLGregorianCalendarFromDate(String date) throws CbrRateException, DatatypeConfigurationException {
        GregorianCalendar gregorianCalendar = new GregorianCalendar();
        if (date != null) {
            try {
                Date parsedDate = formatter.parse(date);
                gregorianCalendar.setTime(parsedDate);
            } catch (ParseException e) {
                throw new CbrRateException("Can't parse date [%s]. Date must be in format yyyy-MM-dd", date);
            }
        }
        DatatypeFactory datatypeFactory = DatatypeFactory.newInstance();
        return datatypeFactory.newXMLGregorianCalendar(gregorianCalendar);
    }

    public void setCbrClient(CbrClient cbrClient) {
        this.cbrClient = cbrClient;
    }

}