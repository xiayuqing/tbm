package org.tbm.admin.controller.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Jason.Xia on 17/6/23.
 */
@Controller
@RequestMapping(value = "/home", produces = "text/html;charset=UTF-8")
public class HomeView {

    private Logger logger = LoggerFactory.getLogger(HomeView.class);


}
