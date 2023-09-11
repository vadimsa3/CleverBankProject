import lombok.*;

import javax.xml.crypto.Data;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@ToString

public class Client {

    private String clientName;
    private Data openDate;
    private String bankName;
    private String passport;

}
