package com.issue.manager.models.constants;

public class MessageConstants {

    public static final String INVITED_TO_PROJECT_MESSAGE = "You are invited to \"%s\" project, would you like to accept the invitation?";

    public static final String INVITED_TO_PROJECT_NOTIFICATION_NAME = "Invited to project";

    public static final String INVITED_TO_PROJECT_SUBJECT = "You have been invited to a new project!";

    public static final String INVITED_TO_PROJECT_EMAIL_MESSAGE =
            """
                    <p>Dear future user,</p>
                    <p>You have been invited to project: <em>"%s" </em> on Project Manager App</p>
                    <p><a href="%s">Register on this link</a></p>
                    Sincerely,\040
                    your favourite Project Manager App!
            """;
}
