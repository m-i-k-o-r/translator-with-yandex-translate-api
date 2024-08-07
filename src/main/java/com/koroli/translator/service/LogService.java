package com.koroli.translator.service;

import com.koroli.translator.model.Notation;
import com.koroli.translator.model.User;
import com.koroli.translator.repository.NotationRepository;
import com.koroli.translator.repository.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.net.InetAddress;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class LogService {

    private static final String[] IP_HEADERS = {
            "X-Forwarded-For",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR"
    };

    private final UserRepository userRepository;
    private final NotationRepository notationRepository;

    public String getIP(HttpServletRequest request) {
        for (String header : IP_HEADERS) {
            String ipList = request.getHeader(header);
            if (ipList != null && !ipList.isEmpty() && !"unknown".equalsIgnoreCase(ipList)) {
                return ipList.split(",")[0];
            }
        }
        String ip = request.getRemoteAddr();
        if (ip.equals("127.0.0.1") || ip.equals("0:0:0:0:0:0:0:1")) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return ip;
    }

    public void createLog(String textInput,
                        String textOutput,
                        String userIp) {
        try {
            userRepository.findById(userIp);
        } catch (NullPointerException e) {
            userRepository.save(User.builder()
                    .id(userIp)
                    .build());
        }

        notationRepository.save(Notation.builder()
                        .sourceText(textInput)
                        .targetText(textOutput)
                        .build(),
                userIp);
    }

    public Set<Notation> getLogsByUserId(String userId) {
        return notationRepository.findAllByUser(userId);
    }
}
