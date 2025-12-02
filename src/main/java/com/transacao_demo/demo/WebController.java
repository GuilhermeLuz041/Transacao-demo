package com.transacao_demo.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class WebController {

    @Autowired
    private TransactionalService service;

    @Autowired
    private ProductRepository repository;

    @GetMapping
    public String index(Model model) {
        long count = repository.count();
        model.addAttribute("count", count);
        model.addAttribute("products", repository.findAll());
        model.addAttribute("logMessage", "Clique para iniciar a simulação de transação.");
        return "index";
    }

    @PostMapping("/simular-rollback")
    public String simularRollback(Model model) {
        String logMessage = "";
        
        try {
            service.simularTransacaoComRollback();
            logMessage = "Transação concluída com SUCESSO. (COMMIT)";
        } catch (RuntimeException e) {
            logMessage = "Transação FALHOU (RuntimeException). O Spring forçou o ROLLBACK.";
        }
        
        model.addAttribute("count", repository.count());
        model.addAttribute("products", repository.findAll());
        model.addAttribute("logMessage", logMessage);

        return "index"; 
    }
}
