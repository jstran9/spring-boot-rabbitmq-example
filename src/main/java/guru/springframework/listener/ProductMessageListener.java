package guru.springframework.listener;

import guru.springframework.domain.Product;
import guru.springframework.repositories.ProductRepository;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

/**
 * This is the queue listener class, its receiveMessage() method ios invoked with the
 * message as the parameter.
 */
@Component
public class ProductMessageListener {

    private ProductRepository productRepository;

    private static final Logger log = Logger.getLogger(ProductMessageListener.class);

    public ProductMessageListener(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    /**
     * This method is invoked whenever any new message is put in the queue.
     * See {@link guru.springframework.SpringBootRabbitMQApplication} for more details
     * @param message
     */
    public void receiveMessage(Map<String, String> message) {
        log.info("Received <" + message + ">");
        Long id = Long.valueOf(message.get("id"));
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isPresent()) {
            Product product = productOptional.get();
            product.setMessageReceived(true);
            product.setMessageCount(product.getMessageCount() + 1);

            productRepository.save(product);
            log.info("Message processed...");
        }
        else {
            log.info("message could not be processed!");
        }
    }
}
