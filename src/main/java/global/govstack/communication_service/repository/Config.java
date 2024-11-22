package global.govstack.communication_service.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "config")
public class Config {
    @Id
    private String config_key;
    private String config_value;
}
