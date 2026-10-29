package fu.edu.sba301.slot33.service;

import java.io.IOException;
import java.nio.file.Path;

public interface IExportService {
    long exportProductsToJsonLines(Path targetFile) throws IOException;
}
