package com.transacao_demo.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class TransactionalService {

    private static final Logger AUDIT_LOG = LoggerFactory.getLogger(TransactionalService.class);

    @Autowired
    private ProductRepository repository;

    @Transactional
    public void simularTransacaoComRollback() {
        AUDIT_LOG.info("AUDIT: Transação ID {} INICIADA. Método @Transactional ativado.", System.currentTimeMillis());
        
        try {
            Product p1 = new Product();
            p1.setName("Item A - SALVO TEMPORARIAMENTE");
            p1.setStock(10);
            repository.save(p1);
            System.out.println("✅ Item A salvo na sessão da transação. ID temporário: " + p1.getId());
            AUDIT_LOG.info("AUDIT: Inserção do Item A (ID {}) concluída na sessão transacional.", p1.getId());
        } catch (Exception e) {
            AUDIT_LOG.error("AUDIT: Falha ao inserir Item A. Rollback será iniciado.", e);
            throw new RuntimeException("Falha na Operação A.");
        }


        try {
            Product p2 = new Product();
            p2.setName("Item B - FALHA ESPERADA");
            p2.setStock(5);
            repository.save(p2);
            System.out.println("✅ Item B salvo na sessão da transação. ID temporário: " + p2.getId());
            AUDIT_LOG.info("AUDIT: Inserção do Item B (ID {}) concluída na sessão transacional.", p2.getId());
        } catch (Exception e) {
            AUDIT_LOG.error("AUDIT: Falha ao inserir Item B. Rollback será iniciado.", e);
            throw new RuntimeException("Falha na Operação B.");
        }

        System.out.println("\n🔥 Forçando um erro (RuntimeException) para INICIAR O ROLLBACK...");
        AUDIT_LOG.error("AUDIT: Falha de negócio detectada. Lançando RuntimeException para forçar ROLLBACK.");
        
        throw new RuntimeException("Falha de Negócio Simulado: Transação interrompida!");

    }
}