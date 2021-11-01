package wang.zhongpin.pi.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import java.util.List;

@Component
public class ApiKeyService {
    @Value("${pi.apiKey.piAPI}")
    private List<String> apiKeyList;
    public boolean validate(String apiKey) {
        return apiKeyList.contains(apiKey);
    }
}
