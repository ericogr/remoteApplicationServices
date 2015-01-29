package br.com.rapps;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;
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
    private final Queue<DeferredResult<Integer>> requests = new ConcurrentLinkedQueue<>();
    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    
    @RequestMapping(value = "adicionar", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void adicionar() {
        try {
            MongoClient mongoClient = new MongoClient();
            DB db = mongoClient.getDB( "test" );
            DBCollection coll = db.getCollection("teste");
            
            mongoClient.setWriteConcern(WriteConcern.JOURNALED);
            
            BasicDBObject doc = new BasicDBObject("name", "MongoDB")
                    .append("type", "database")
                    .append("count", 1)
                    .append("info", new BasicDBObject("x", 203).append("y", 102));
            coll.insert(doc);
        }
        catch (UnknownHostException ex) {
            logger.error("Erro ao adicionar usuario no mongo db", ex);
        }
    }
    
    @RequestMapping(value = "adicionar2", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void adicionar2() {
        try {
            MongoClient mongoClient = new MongoClient();
            Datastore datastore = new Morphia().createDatastore(mongoClient, "test");
            
            Pessoa pessoa = new Pessoa();
            pessoa.setIdade(34);
            pessoa.setNome("erico");
            pessoa.setSobrenome("gr");
            
            datastore.save(pessoa);
            
        }
        catch (UnknownHostException ex) {
            logger.error("Erro ao adicionar usuario no mongo db", ex);
        }
    }

    @RequestMapping(value = "somar", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public int somar(@RequestParam int a, @RequestParam int b, Principal principal) {
        logger.info("Usuário consumindo o servico: " + principal.getName());
        return a + b;
    }
    
    @RequestMapping(value = "somar-non-blocking", method = RequestMethod.GET)
    public ListenableFuture<Integer> somarNonBlockingProcessing(@RequestParam int a, @RequestParam int b) {
        final ListenableFuture<Integer> responseFuture = new ListenableFutureTask<>(() -> a + b);
        
        return responseFuture;
    }
}