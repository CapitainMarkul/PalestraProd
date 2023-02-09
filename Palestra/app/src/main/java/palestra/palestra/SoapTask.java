package palestra.palestra;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

/**
 * Created by Dmitry on 15.04.2017.
 */
public class SoapTask {

    private SoapObject request;

    public SoapTask(String namespace, String methodName){
        request = new SoapObject(namespace, methodName);
    }
    public void AddProperty(String nameProperty, String value){
        PropertyInfo property = new PropertyInfo();
        property.setName(nameProperty);
        property.setValue(new String(value));
        request.addProperty(property);
    }
    public void AddProperty(String nameProperty, Double value){
        PropertyInfo property = new PropertyInfo();
        property.setName(nameProperty);
        property.setValue(value);
        request.addProperty(property);
    }
//    public void AddProperty(String nameProperty, Timestamp value){
//        PropertyInfo property = new PropertyInfo();
//        property.setName(nameProperty);
//        property.setValue(value);
//        request.addProperty(property);
//    }
    public void AddProperty(String nameProperty, int value){
        PropertyInfo property = new PropertyInfo();
        property.setName(nameProperty);
        property.setValue(new Integer(value));
        request.addProperty(property);
    }
    public SoapObject startSoap(String url, String SoapAction){
        SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
        envelope.dotNet = true;

        envelope.setOutputSoapObject(request);
        HttpTransportSE androidHttpTransport = new HttpTransportSE(url);
        androidHttpTransport.setXmlVersionTag("<?xml version=\"1.0\" encoding=\"utf-8\"?>");

        try {
            androidHttpTransport.call(SoapAction, envelope);
        } catch (Exception e) {
            return null;
        }
        return (SoapObject) envelope.bodyIn;
    }
}
