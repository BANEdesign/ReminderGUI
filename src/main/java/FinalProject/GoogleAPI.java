package FinalProject;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.client.util.DateTime;

import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.calendar.model.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class GoogleAPI {
    /**
     * Recieved Verification info might be "code=4/xgYp4ke2UffiaGxCoToIxn5DU6-la90QzP8wtCF6VrQ#"
     */
    /**
     * OAuth 2.0 authorization*
     *
     * when the user wants to add an event or get up their personal calender, direct them
     * to a Google URL,  the URL includes query parameters that indicate the type of access
     * being requested. Google handles the user authentication, session selection, and
     * user consent. The result is an authorization code, which the application can
     * exchange for an access token and a refresh token.
     *
     * Loopback IP address
     *
     *To receive the authorization code using this URL, your application must be listening
     * on the local web server.
     *
     * See the redirect_uri parameter definition for more information about the loopback
     * IP address. It is also possible to use localhost in place of the loopback IP
     */

    private static final String APPLICATION_NAME =
            "My Project";

    /**
     * Directory to store user credentials for this application.
     */
    private static final java.io.File DATA_STORE_DIR =
            new java.io.File("ReminderProject");

    /**
     * Global instance of the {@link FileDataStoreFactory}.
     */
    private static FileDataStoreFactory DATA_STORE_FACTORY;

    /**
     * Global instance of the JSON factory.
     */
    private static final JsonFactory JSON_FACTORY =
            JacksonFactory.getDefaultInstance();

    /**
     * Global instance of the HTTP transport.
     */
    private static HttpTransport HTTP_TRANSPORT;

    /**
     * Global instance of the scopes required by this quickstart.
     * <p>
     * If modifying these scopes, delete your previously saved credentials
     * at ~/.credentials/calendar-java-quickstart
     */
    private static final List<String> SCOPES =
            Arrays.asList(CalendarScopes.CALENDAR, CalendarScopes.CALENDAR_READONLY);

    static {
        try {
            HTTP_TRANSPORT = GoogleNetHttpTransport.newTrustedTransport();
            DATA_STORE_FACTORY = new FileDataStoreFactory(DATA_STORE_DIR);
        } catch (Throwable t) {
            t.printStackTrace();
            System.exit(1);
        }
    }

    /**
     * Creates an authorized Credential object.
     *
     * @return an authorized Credential object.
     * throws IOException
     */
    private static Credential authorize() throws IOException {
        // Load client secrets.
        InputStream in =
                Calendar.class.getResourceAsStream("/client_secret.json");
        GoogleClientSecrets clientSecrets =
                GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(in));

        // Build flow and trigger user authorization request.
        GoogleAuthorizationCodeFlow flow =
                new GoogleAuthorizationCodeFlow.Builder(
                        HTTP_TRANSPORT, JSON_FACTORY, clientSecrets, SCOPES)
                        .setDataStoreFactory(DATA_STORE_FACTORY)
                        .build();
        new AuthorizationCodeInstalledApp(
                flow, new LocalServerReceiver()).authorize("bbaines09@gmail.com");
        System.out.println(
                "Credentials saved to " + DATA_STORE_DIR.getAbsolutePath());
        return new AuthorizationCodeInstalledApp(flow,
                new LocalServerReceiver()).authorize("qaalibomer@gmail.com");
    }

    /**
     * Build and return an authorized Calendar client service.
     *
     * @return an authorized Calendar client service
     * throws IOException
     */
    private static com.google.api.services.calendar.Calendar

    getCalendarService() throws IOException {
        Credential credential = authorize();
        return new com.google.api.services.calendar.Calendar.Builder(
                HTTP_TRANSPORT, JSON_FACTORY, credential)
                .setApplicationName(APPLICATION_NAME)
                .build();
    }

    public static void main(String[] args) throws IOException {
        // Build a new authorized API client service.
        // Note: Do not confuse this class with the
        //   com.google.api.services.calendar.model.Calendar class.
        com.google.api.services.calendar.Calendar service =
                getCalendarService();

        // List the next 10 events from the primary calendar.
        DateTime now = new DateTime(System.currentTimeMillis());
        Events events = service.events().list("primary")
                .setMaxResults(10)
                .setTimeMin(now)
                .setOrderBy("startTime")
                .setSingleEvents(true)
                .execute();
        List<Event> items = events.getItems();
        if (items.size() == 0) {
            System.out.println("No upcoming events found.");
        } else {
            System.out.println("Upcoming events");
            for (com.google.api.services.calendar.model.Event event : items) {
                DateTime start = event.getStart().getDateTime();
                if (start == null) {
                    start = event.getStart().getDate();
                }
                System.out.printf("%s (%s)\n", event.getSummary(), start);
            }
        }
    }

    protected static void createEvent(String task, String date) {
        try {

            com.google.api.services.calendar.Calendar service =
                    getCalendarService();

            Event event = new Event()
                    .setSummary(task);

            DateTime start = new DateTime(date + "T09:00:00-05:30");
            EventDateTime eventStart = new EventDateTime()
                    .setDateTime(start)
                    .setTimeZone("America/Chicago");
            event.setStart(eventStart);

            DateTime eventEnd = new DateTime(date + "T09:00:00-05:30");
            EventDateTime end = new EventDateTime()
                    .setDateTime(eventEnd)
                    .setTimeZone("America/Chicago");
            event.setEnd(end);

            //you could also set the time for the reminder here, otherwise it uses the default

            Event.Reminders reminder = new Event.Reminders().setUseDefault(true);
            event.setReminders(reminder);

            String calendarId = "primary";
            try {
                event = service.events().insert(calendarId, event).execute();
                System.out.printf("Event created: %s\n", event.getHtmlLink());

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }catch (IOException io) {
            System.out.println("Error connecting to Calender");
            io.printStackTrace();
        }
    }
}