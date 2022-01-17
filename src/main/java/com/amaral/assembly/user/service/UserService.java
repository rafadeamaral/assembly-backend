package com.amaral.assembly.user.service;

import com.amaral.assembly.common.exception.ServiceException;
import com.amaral.assembly.user.domain.UserResponse;
import com.amaral.assembly.user.domain.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class UserService {

    @Value("${user.info.endpoint}")
    private String endpoint;

    @Autowired
    private RestTemplate restTemplate;

    public void validateCpf(String cpf) {

        findByCpf(cpf);
    }

    public void validateVote(String cpf) {

        UserResponse userResponse = findByCpf(cpf);

        if (!UserStatus.ABLE_TO_VOTE.equals(userResponse.getStatus())) {

            throw new ServiceException("vote.not.allowed", cpf);
        }
    }

    private UserResponse findByCpf(String cpf) {

        try {

            Map<String, Object> parameters = new HashMap<>();
            parameters.put("cpf", cpf);

            ResponseEntity<UserResponse> response = restTemplate.getForEntity(endpoint, UserResponse.class, parameters);

            if (!HttpStatus.OK.equals(response.getStatusCode())) {

                throw new ServiceException("invalid.cpf");

            } else {

                return response.getBody();
            }

        } catch (HttpClientErrorException e) {

            if (HttpStatus.NOT_FOUND.equals(e.getStatusCode())) {

                throw new ServiceException("invalid.cpf");

            } else {

                throw new ServiceException("cpf.api.problem");
            }
        }
    }

}
