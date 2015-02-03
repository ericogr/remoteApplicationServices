package br.com.rapps;

import java.security.Principal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

@RestController
@RequestMapping(value = "matematica")
public class TesteController {
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final ExecutorService executorService = Executors.newFixedThreadPool(10);
    
    @RequestMapping(value = "somar", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public int somar(@RequestParam int a, @RequestParam int b, Principal principal) {
        logger.info("Usuário consumindo o servico: " + principal.getName());
        return a + b;
    }
    
    @RequestMapping(value = "somar-non-blocking", method = RequestMethod.GET)
    public DeferredResult<Integer> somarNonBlockingProcessing(@RequestParam int a, @RequestParam int b) {
        DeferredResult<Integer> deferredResult = new DeferredResult<>();
        
        executorService.execute(() -> {
            deferredResult.setResult(a + b);
        });
                
        return deferredResult;
    }
}