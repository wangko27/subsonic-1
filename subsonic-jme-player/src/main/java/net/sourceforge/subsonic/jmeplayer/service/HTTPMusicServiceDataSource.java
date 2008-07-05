package net.sourceforge.subsonic.jmeplayer.service;

import net.sourceforge.subsonic.jmeplayer.SettingsController;

import javax.microedition.io.Connector;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * @author Sindre Mehus
 */
public class HTTPMusicServiceDataSource implements MusicServiceDataSource {

    private final SettingsController settingsController;

    public HTTPMusicServiceDataSource(SettingsController settingsController) {
        this.settingsController = settingsController;
    }

    public Reader getIndexesReader() throws Exception {
        String url = getBaseUrl() + "getIndexes.view?u=" + settingsController.getUsername() + "&p=" + settingsController.getPassword();
        InputStream in = Connector.openInputStream(url);
        return new InputStreamReader(in);
    }

    public Reader getMusicDirectoryReader(String path) throws Exception {
        String url = getBaseUrl() + "getMusicDirectory.view?pathUtf8Hex=" + path + "&u=" + settingsController.getUsername() + "&p=" + settingsController.getPassword();

        int player = settingsController.getPlayer();
        if (player > 0) {
            url += "&player=" + player;
        }

        InputStream in = Connector.openInputStream(url);
        return new InputStreamReader(in);
    }

    private String getBaseUrl() {
        String baseUrl = settingsController.getBaseUrl();
        if (!baseUrl.endsWith("/")) {
            baseUrl += "/";
        }
        return baseUrl + "mobile/";
    }
}
