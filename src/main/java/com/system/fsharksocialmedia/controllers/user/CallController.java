package com.system.fsharksocialmedia.controllers.user;

import com.system.fsharksocialmedia.documents.SignalMessage;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/user/call")
public class CallController {

    @SendTo("/topic/call")
    public SignalMessage handleCall(SignalMessage signalMessage) {
        return signalMessage;
    }

}
