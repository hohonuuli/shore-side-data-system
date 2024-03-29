/*
 * Copyright 2009 MBARI
 *
 * Licensed under the GNU LESSER GENERAL PUBLIC LICENSE, Version 2.1 
 * (the "License"); you may not use this file except in compliance 
 * with the License. You may obtain a copy of the License at
 *
 * http://www.gnu.org/copyleft/lesser.html
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package moos.ssds.services.servlet.data;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.TimeZone;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import moos.ssds.metadata.IMetadataObject;
import moos.ssds.services.servlet.util.MethodProxy;
import moos.ssds.services.servlet.util.ServletFaultHandler;
import moos.ssds.services.servlet.util.ServletUtils;
import moos.ssds.util.XmlDateFormat;

import org.apache.log4j.Logger;

/**
 * @author kgomes
 * @version 1.0
 * @web.servlet name="DataAccessServlet" display-name="Servlet to interact with
 *              data services"
 * @web.servlet-mapping url-pattern="/DataAccessServlet/*"
 * @web.servlet-mapping url-pattern="*.DataAccessServlet"
 * @web.servlet-mapping url-pattern="/DataAccessServlet"
 */
public class DataAccessServlet extends HttpServlet {

    /**
     * Override the init method to do the one time setup things
     */
    public void init(ServletConfig servletConfig) throws ServletException {
        // Call the init on HttpServlet
        super.init(servletConfig);
    }

    /**
     * This is the doPost method defined in the HTTPServlet. In this case, it
     * simply calls the doGet method passing the request response pair
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
        doGet(request, response);
    }

    /**
     * This is the doGet method where the real stuff happens
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {

        // Find the response type
        String responseType = this.setResponseType(request, response);
        logger.debug("Query string: " + request.getQueryString());

        // Get the output writer
        PrintWriter out = new PrintWriter(response.getOutputStream());

        // First let's grab the query parameters
        Map parameterMap = request.getParameterMap();

        // Print headers if needed
        this.printHeader(out, responseType);

        // Find the delimiter is specified by the caller
        String delimiter = this.getParameter(parameterMap, "delimiter");
        if (delimiter == null)
            delimiter = ServletUtils.DEFAULT_DELIMITER;

        // Setup the objects for invoking
        Object objectToInvokeOn = null;
        // IMetadataAccess theAccessObjectToUse = null;

        // A place to store the result
        Object result = null;

        // Now see if the object to invoke on is specified
        String objectToInvokeOnString = null;
        objectToInvokeOnString = this.getParameter(parameterMap,
            "objectToInvokeOn");
        if ((objectToInvokeOnString == null)
            || (objectToInvokeOnString.equals(""))) {
            out.print(ServletFaultHandler.formatFaultMessage(
                ServletFaultHandler.FAULT_CODE_SENDER, "Missing Parameter",
                "You did not specify the objectToInvokeOn parameter",
                "Please specify the objectToInvokeOn parameter so the method"
                    + " may be called correctly", responseType));
        } else {

            // First thing to do is get the EJB interface for the data service
            // to call
            try {
                objectToInvokeOn = ServletUtils
                    .getLocalDataAccessInterface(Class
                        .forName("moos.ssds.services.data."
                            + objectToInvokeOnString));
            } catch (ClassNotFoundException e) {
                logger.error("ClassNotFoundException: " + e.getMessage());
            }

            if (objectToInvokeOn != null) {
                // First see if the method is specified, if not return a fault
                String methodName = null;
                methodName = this.getParameter(parameterMap, "method");
                if ((methodName == null) || (methodName.equals(""))) {
                    out.print(ServletFaultHandler.formatFaultMessage(
                        ServletFaultHandler.FAULT_CODE_SENDER,
                        "Missing Parameter",
                        "You did not specify the method parameter",
                        "Available Methods are:"
                            + ServletUtils.getMethodListing(objectToInvokeOn
                                .getClass(), responseType), responseType));
                } else {
                    // Now construct the method proxy
                    MethodProxy mp = new MethodProxy(objectToInvokeOn, request);

                    // Check to see if we are good to go
                    if (mp.isOkToCallMethod()) {
                        result = mp.invokeMethod();
                        // Check MethodProxy to make sure it went OK
                        if (!mp.isOkToCallMethod()) {
                            out.print(ServletFaultHandler.formatFaultMessage(mp
                                .getFaultCode(), mp.getFaultSubcode(), mp
                                .getFaultReason(), mp.getFaultDetail(),
                                responseType));
                        } else {
                            this.printResult(out, result, responseType,
                                delimiter);
                        }
                    } else {
                        out.print(ServletFaultHandler.formatFaultMessage(mp
                            .getFaultCode(), mp.getFaultSubcode(), mp
                            .getFaultReason(), mp.getFaultDetail()
                            + "Available Methods are:"
                            + ServletUtils.getMethodListing(objectToInvokeOn
                                .getClass(), responseType), responseType));
                    }
                }
            } else {
                // TODO kgomes print some error message
            }
        }
        // Print the footer if needed
        this.printFooter(out, responseType);

        // Now flush the output
        out.flush();
    }

    /**
     * This method takes in a request/response HTTP pair and sets the response
     * to the correct type based on the incoming request
     * 
     * @param request
     * @param response
     * @return the respone format that was set
     */
    private String setResponseType(HttpServletRequest request,
        HttpServletResponse response) {

        // First let's grab the query parameters
        Map parameterMap = request.getParameterMap();

        // Now look for the responseType parameter
        String responseType = this.getParameter(parameterMap, "responseType");
        if (responseType != null) {
            if (responseType.equals(ServletUtils.TEXT_FORMAT)
                || responseType.equals(ServletUtils.STRING_FORMAT)) {
                response.setContentType("text/plain");
            } else if (responseType.equals(ServletUtils.HTML_FORMAT)) {
                response.setContentType("text/html");
            } else if (responseType.equals(ServletUtils.XML_FORMAT)) {
                response.setContentType("text/xml");
            } else {
                response.setContentType("text/html");
            }
        } else {
            response.setContentType("text/html");
            responseType = ServletUtils.HTML_FORMAT;
        }
        return responseType;
    }

    /**
     * This method prints header information on the printWriter if needed
     * 
     * @param printWriter
     * @param responseFormat
     */
    private void printHeader(PrintWriter printWriter, String responseFormat) {
        if (responseFormat != null) {
            if (responseFormat.equals(ServletUtils.TEXT_FORMAT)
                || responseFormat.equals(ServletUtils.STRING_FORMAT)) {} else if (responseFormat
                .equals(ServletUtils.HTML_FORMAT)) {
                printWriter.print("<html>");
            } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                printWriter
                    .println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            } else {
                printWriter.print("<html>");
            }
        } else {
            printWriter.print("<html>");
        }
    }

    /**
     * This method prints out the results of the method call to the output
     * stream in the correct format
     * 
     * @param printWriter
     * @param result
     * @param responseFormat
     */
    private void printResult(PrintWriter printWriter, Object result,
        String responseFormat, String delimiter) {
        if (responseFormat.equals(ServletUtils.HTML_FORMAT)) {
            printWriter.println("<b>Result(s):</b><br>");
        } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
            printWriter.print("<results>");
        }
        if (result != null) {
            // First check to see if it is a collection
            if (result instanceof Collection) {
                if (responseFormat.equals(ServletUtils.HTML_FORMAT))
                    printWriter.println("<ol>");
                Collection returnCollection = (Collection) result;
                Iterator it = returnCollection.iterator();
                while (it.hasNext()) {
                    Object tempReturnedObject = it.next();
                    if (responseFormat.equals(ServletUtils.HTML_FORMAT)) {
                        printWriter.print("<li>");
                    } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                        printWriter.print("<result>");
                    }
                    if (tempReturnedObject instanceof IMetadataObject) {
                        String stringRep = ((IMetadataObject) tempReturnedObject)
                            .toStringRepresentation(delimiter);
                        logger.debug("Starting with string : " + stringRep);
                        // Strip out any newlines
                        stringRep = stringRep.replaceAll("\\n", "");
                        logger.debug("After stripping newlines: " + stringRep);
                        printWriter.print(stringRep);
                    } else {
                        printWriter.print(tempReturnedObject.getClass()
                            .getName()
                            + delimiter + tempReturnedObject.toString());
                    }
                    if (responseFormat.equals(ServletUtils.HTML_FORMAT)) {
                        printWriter.println("</li>");
                    } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                        printWriter.print("</result>");
                    } else {
                        printWriter.print("\n");
                    }
                }
                if (responseFormat.equals(ServletUtils.HTML_FORMAT))
                    printWriter.println("</ol>");
            } else if (result instanceof Object[][]) {
                // This means it is a multidimensional array with the first
                // column being time
                if (responseFormat.equals(ServletUtils.HTML_FORMAT))
                    printWriter.println("<ol>");
                Object[][] resultArray = (Object[][]) result;
                logger.debug("Result is array of objects");
                // Loop over the first indexes
                for (int i = 0; i < resultArray.length; i++) {
                    if (responseFormat.equals(ServletUtils.HTML_FORMAT)) {
                        printWriter.print("<li>");
                    } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                        printWriter.print("<result>");
                    }
                    // Now loop over the second array
                    for (int j = 0; j < resultArray[i].length; j++) {
                        // Convert the time to something more readable
                        if (j == 0) {
                            Calendar tempCal = Calendar.getInstance();
                            tempCal.setTimeZone(TimeZone.getTimeZone("GMT"));
                            tempCal.setTimeInMillis(((Long) resultArray[i][j])
                                .longValue());
                            printWriter.print(xmlDateFormat.format(tempCal
                                .getTime()));
                        } else {
                            printWriter.print(resultArray[i][j].toString());
                        }
                        if (j != (resultArray[i].length - 1)) {
                            printWriter.print(delimiter);
                        }
                    }
                    if (responseFormat.equals(ServletUtils.HTML_FORMAT)) {
                        printWriter.println("</li>");
                    } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                        printWriter.print("</result>");
                    } else {
                        printWriter.print("\n");
                    }
                }
            } else {
                if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                    printWriter.print("<result>");
                }
                if (result instanceof IMetadataObject) {
                    String stringRep = ((IMetadataObject) result)
                        .toStringRepresentation(delimiter);
                    logger.debug("Starting with string : " + stringRep);
                    // Strip out any newlines
                    stringRep = stringRep.replaceAll("\\n", "");
                    logger.debug("After stripping newlines: " + stringRep);
                    printWriter.print(stringRep);
                } else {
                    printWriter.print(result.getClass().getName() + delimiter
                        + result.toString());
                }
                if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                    printWriter.print("</result>");
                }
            }
        } else {
            if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
                printWriter.print("<result>null</result>");
            } else {
                printWriter.print("null");
            }
        }
        if (responseFormat.equals(ServletUtils.XML_FORMAT)) {
            printWriter.print("</results>");
        }
    }

    /**
     * This method prints header information on the printWriter if needed
     * 
     * @param printWriter
     * @param responseFormat
     */
    private void printFooter(PrintWriter printWriter, String responseFormat) {
        if (responseFormat.equals(ServletUtils.TEXT_FORMAT)
            || responseFormat.equals(ServletUtils.STRING_FORMAT)) {} else if (responseFormat
            .equals(ServletUtils.HTML_FORMAT)) {
            printWriter.print("</html>");
        } else if (responseFormat.equals(ServletUtils.XML_FORMAT)) {} else {
            printWriter.print("</html>");
        }
    }

    /**
     * This method looks up the value in the parameter map with the given name
     * 
     * @param parameterMap
     * @param parameterName
     * @return the parameter value (or null)
     */
    private String getParameter(Map parameterMap, String parameterName) {
        if (parameterName == null)
            return null;
        // The value to return
        String parameterValue = null;
        // If one was specified in the query string, grab the first one
        if (parameterMap.containsKey(parameterName)) {
            String[] parameterValues = (String[]) parameterMap
                .get(parameterName);
            if (parameterValues.length > 0) {
                parameterValue = parameterValues[0];
            }
        }
        return parameterValue;
    }

    /**
     * This is the <code>serialVersionUID</code> that is fixed to control
     * serialization versions of the class.
     */
    private static final long serialVersionUID = 1L;

    /**
     * A Date formatter
     */
    private XmlDateFormat xmlDateFormat = new XmlDateFormat();

    /** A log4j logger */
    static Logger logger = Logger.getLogger(DataAccessServlet.class);
}
