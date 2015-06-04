package bynull.realty.data.business.configs;

import javax.persistence.*;

/**
 * Created by dionis on 04/06/15.
 */
@Entity
@Table(name = "server_settings")
public class ServerSetting {
    @javax.persistence.Id
    @Column(name = "id")
    @Enumerated(EnumType.STRING)
    private ServerSetting.Id id;

    @Column(name = "value")
    private String value;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ServerSetting)) return false;

        ServerSetting that = (ServerSetting) o;

        if (getId() != that.getId()) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return getId() != null ? getId().hashCode() : 0;
    }

    public static enum Id {
        VK_TOKEN
    }
}
