package br.com.rapps;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import com.mongodb.WriteConcern;
import java.net.UnknownHostException;
import java.security.Principal;
import java.util.Timer;
import java.util.TimerTask;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
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
    private Logger logger = LoggerFactory.getLogger(getClass());
    private final Timer timer = new Timer();
    
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
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
    }

    @RequestMapping(value = "somar", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public int somar(@RequestParam int a, @RequestParam int b, Principal principal) {
        logger.info("Usuário consumindo o servico: " + principal.getName());
        return a + b;
    }
    
    @RequestMapping(value = "/somar-blocking", method = RequestMethod.GET)
    public Integer somarBlockingProcessing(@RequestParam int a, @RequestParam int b) {
        try {
            //long x = (long)(Math.random() * 5000);
            Thread.sleep(5000);
        } 
        catch (InterruptedException e) {
        }
        
        return a + b;
    }

    @RequestMapping(value = "/somar-non-blocking", method = RequestMethod.GET)
    public DeferredResult<Integer> somarNonBlockingProcessing(@RequestParam int a, @RequestParam int b) {

        DeferredResult<Integer> deferredResult = new DeferredResult<>();
        ProcessingTask task = new ProcessingTask(a, b, deferredResult);

        // Schedule the task for asynch completion in the future
        //long x = (long)(Math.random() * 5000);
        timer.schedule(task, 5000);

        // Return to let go of the precious thread we are holding on to...
        return deferredResult;
    }

    class ProcessingTask extends TimerTask {
        private final Logger LOG = LoggerFactory.getLogger(ProcessingTask.class);
        private final int a, b;
        private final DeferredResult<Integer> deferredResult;

        public ProcessingTask(int a, int b, DeferredResult<Integer> deferredResult) {
            this.a = a;
            this.b = b;
            this.deferredResult = deferredResult;
        }

        @Override
        public void run() {
            if (deferredResult.isSetOrExpired()) {
                LOG.warn("Processing of non-blocking request already expired");
            } else {
                boolean deferredStatus = deferredResult.setResult(new Integer(this.a + this.b));
                LOG.debug("Processing of non-blocking done, deferredStatus = {}", deferredStatus);
            }
        }
    }
}
