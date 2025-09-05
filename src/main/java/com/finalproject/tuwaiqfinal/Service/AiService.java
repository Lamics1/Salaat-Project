package com.finalproject.tuwaiqfinal.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.finalproject.tuwaiqfinal.Api.ApiException;
import com.finalproject.tuwaiqfinal.DTOout.AnalyseGameDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class AiService {

    private final ChatClient chatClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public AiService(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    public AnalyseGameDTO analyzeImage(byte[] imageBytes) {
        if (imageBytes == null || imageBytes.length == 0) {
            throw new ApiException("image bytes are empty");
        }

        String systemPrompt = """
                أنت مساعد بصري متخصص في تحليل صورة واحدة فقط.
                أجب بالعربية فقط.
                أعد مخرجاتك بصيغة JSON فقط دون أي نص إضافي.
                """;

        String userPrompt = """
                حلّل الصورة وأعد JSON صالح فقط:
                
                {
                  "اسم_اللعبة": "<أقرب اسم لعبة يمكن تحديده>",
                  "عدد_اللاعبين": "<العدد المعتاد أو نطاق اللاعبين>",
                  "كيفية_اللعب": "<شرح تفصيلي: الهدف، الخطوات الرئيسية، القواعد الأساسية>",
                  "بدائل_ممكنة": ["لعبة بديلة 1", "لعبة بديلة 2"],
                  "مستوى_الصعوبة": "<سهل / متوسط / صعب>",
                  "نصائح_للعبة": ["نصيحة 1", "نصيحة 2", "نصيحة 3"]
                }
                
                قواعد:
                - لا تُرجع أي نص خارج JSON.
                - إذا كانت الأدوات شائعة وتُستخدم في أكثر من لعبة (مثل أوراق كوتشينة أو نرد)، اذكر اسم اللعبة الأكثر ترجيحًا وضع البدائل في "بدائل_ممكنة".
                - لا تترك أي حقل فارغ. إذا غير معروف، ضع "غير محدد".
                - "مستوى_الصعوبة" يُقيَّم بشكل تقريبي بناءً على شيوع القواعد وسهولة تعلمها.
                - "نصائح_للعبة" يجب أن تكون عملية ومباشرة (مثل: التركيز على قطع الخصم، إدارة الأوراق، أو التخطيط للحركات القادمة).
                """;


        ByteArrayResource resource = new ByteArrayResource(imageBytes) {
            @Override
            public String getFilename() {
                return "upload.png";
            }
        };

        String content = chatClient
                .prompt()
                .options(OpenAiChatOptions.builder()
                        .model("gpt-4o")
                        .temperature(0.2)
                        .build())
                .system(systemPrompt)
                .user(u -> u.text(userPrompt).media(MediaType.IMAGE_PNG, resource))
                .call()
                .content();


        if (content.startsWith("```")) {
            int first = content.indexOf('{');
            int last = content.lastIndexOf('}');
            if (first >= 0 && last > first) {
                content = content.substring(first, last + 1);
            }
        }


        try {
            return objectMapper.readValue(content, AnalyseGameDTO.class);

        } catch (Exception e) {
            log.error("Failed to analyze image / parse JSON", e);
            return new AnalyseGameDTO(null,null,null,null,null,null);
        }
    }
}
