package fu.edu.sba301.slot33.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fu.edu.sba301.slot33.model.Product;
import fu.edu.sba301.slot33.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ExportService implements IExportService {

    private final ProductRepository productRepository;
    private final ObjectMapper objectMapper;

    public ExportService(ProductRepository productRepository, ObjectMapper objectMapper) {
        this.productRepository = productRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    public long exportProductsToJsonLines(Path targetFile) throws IOException {
        if (targetFile.getParent() != null) {
            Files.createDirectories(targetFile.getParent());
        }

        List<Product> products = productRepository.findAll();
        long count = 0;

        try (BufferedWriter writer = Files.newBufferedWriter(targetFile, StandardCharsets.UTF_8)) {
            for (Product p : products) {
                String jsonLine = objectMapper.writeValueAsString(p);
                writer.write(jsonLine);
                writer.newLine();
                count++;
            }
        }
        return count;
    }
}
