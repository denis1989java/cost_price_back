package com.monich.cost_price.api.controller;

import com.monich.cost_price.api.dto.account.AccountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = "/api/account", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@RequiredArgsConstructor
public class AccountController {


    @RequestMapping(method = RequestMethod.GET)
    public List<AccountDto> list() {

        return new ArrayList<>();
    }

}
