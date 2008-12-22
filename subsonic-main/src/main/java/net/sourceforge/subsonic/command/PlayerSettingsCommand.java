package net.sourceforge.subsonic.command;

import net.sourceforge.subsonic.controller.PlayerSettingsController;
import net.sourceforge.subsonic.domain.CoverArtScheme;
import net.sourceforge.subsonic.domain.Player;
import net.sourceforge.subsonic.domain.TranscodeScheme;
import net.sourceforge.subsonic.domain.Transcoding;
import net.sourceforge.subsonic.domain.PlayerTechnology;

import java.util.Date;

/**
 * Command used in {@link PlayerSettingsController}.
 *
 * @author Sindre Mehus
 */
public class PlayerSettingsCommand {
    private String playerId;
    private String name;
    private String description;
    private String type;
    private Date lastSeen;
    private boolean isDynamicIp;
    private boolean isAutoControlEnabled;
    private String coverArtSchemeName;
    private String technologyName;
    private String transcodeSchemeName;
    private boolean transcodingSupported;
    private String transcodeDirectory;
    private Transcoding[] allTranscodings;
    private int[] activeTranscodingIds;
    private EnumHolder[] technologyHolders;
    private EnumHolder[] transcodeSchemeHolders;
    private EnumHolder[] coverArtSchemeHolders;
    private Player[] players;
    private boolean isAdmin;
    private boolean isReloadNeeded;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Date lastSeen) {
        this.lastSeen = lastSeen;
    }

    public boolean isDynamicIp() {
        return isDynamicIp;
    }

    public void setDynamicIp(boolean dynamicIp) {
        isDynamicIp = dynamicIp;
    }

    public boolean isAutoControlEnabled() {
        return isAutoControlEnabled;
    }

    public void setAutoControlEnabled(boolean autoControlEnabled) {
        isAutoControlEnabled = autoControlEnabled;
    }

    public String getCoverArtSchemeName() {
        return coverArtSchemeName;
    }

    public void setCoverArtSchemeName(String coverArtSchemeName) {
        this.coverArtSchemeName = coverArtSchemeName;
    }

    public String getTranscodeSchemeName() {
        return transcodeSchemeName;
    }

    public void setTranscodeSchemeName(String transcodeSchemeName) {
        this.transcodeSchemeName = transcodeSchemeName;
    }

    public boolean isTranscodingSupported() {
        return transcodingSupported;
    }

    public void setTranscodingSupported(boolean transcodingSupported) {
        this.transcodingSupported = transcodingSupported;
    }

    public String getTranscodeDirectory() {
        return transcodeDirectory;
    }

    public void setTranscodeDirectory(String transcodeDirectory) {
        this.transcodeDirectory = transcodeDirectory;
    }

    public Transcoding[] getAllTranscodings() {
        return allTranscodings;
    }

    public void setAllTranscodings(Transcoding[] allTranscodings) {
        this.allTranscodings = allTranscodings;
    }

    public int[] getActiveTranscodingIds() {
        return activeTranscodingIds;
    }

    public void setActiveTranscodingIds(int[] activeTranscodingIds) {
        this.activeTranscodingIds = activeTranscodingIds;
    }

    public EnumHolder[] getTechnologyHolders() {
         return technologyHolders;
     }

     public void setTechnologies(PlayerTechnology[] technologies) {
         technologyHolders = new EnumHolder[technologies.length];
         for (int i = 0; i < technologies.length; i++) {
             PlayerTechnology technology = technologies[i];
             technologyHolders[i] = new EnumHolder(technology.name(), technology.toString());
         }
     }

     public EnumHolder[] getTranscodeSchemeHolders() {
        return transcodeSchemeHolders;
    }

    public void setTranscodeSchemes(TranscodeScheme[] transcodeSchemes) {
        transcodeSchemeHolders = new EnumHolder[transcodeSchemes.length];
        for (int i = 0; i < transcodeSchemes.length; i++) {
            TranscodeScheme scheme = transcodeSchemes[i];
            transcodeSchemeHolders[i] = new EnumHolder(scheme.name(), scheme.toString());
        }
    }

    public EnumHolder[] getCoverArtSchemeHolders() {
        return coverArtSchemeHolders;
    }

    public void setCoverArtSchemes(CoverArtScheme[] coverArtSchemes) {
        coverArtSchemeHolders = new EnumHolder[coverArtSchemes.length];
        for (int i = 0; i < coverArtSchemes.length; i++) {
            CoverArtScheme scheme = coverArtSchemes[i];
            coverArtSchemeHolders[i] = new EnumHolder(scheme.name(), scheme.toString());
        }
    }

    public String getTechnologyName() {
        return technologyName;
    }

    public void setTechnologyName(String technologyName) {
         this.technologyName = technologyName;
     }

     public Player[] getPlayers() {
        return players;
    }

    public void setPlayers(Player[] players) {
        this.players = players;
    }

    public boolean isAdmin() {
        return isAdmin;
    }

    public void setAdmin(boolean admin) {
        isAdmin = admin;
    }

    public boolean isReloadNeeded() {
        return isReloadNeeded;
    }

    public void setReloadNeeded(boolean reloadNeeded) {
        isReloadNeeded = reloadNeeded;
    }

    /**
     * Holds the transcoding and whether it is active for the given player.
     */
    public static class TranscodingHolder {
        private Transcoding transcoding;
        private boolean isActive;

        public TranscodingHolder(Transcoding transcoding, boolean isActive) {
            this.transcoding = transcoding;
            this.isActive = isActive;
        }

        public Transcoding getTranscoding() {
            return transcoding;
        }

        public boolean isActive() {
            return isActive;
        }
    }
}