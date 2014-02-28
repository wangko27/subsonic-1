<script type="text/javascript">
/**
 * global variables
 */
var mediaSession = null;
var currentVolume = 0.5;
var muted = false;
var progressFlag = 1;
var mediaCurrentTime = 0;
var castSession = null;
var playing = true;

/*
  TODO: Set volume slider position
  TODO: Util.getLocalIp() performance.
  TODO: Only init if player type is "web".
  TODO: Host google js locally.
  TODO: Use similar graphics for next/prev buttons.
 */

if (!chrome.cast || !chrome.cast.isAvailable) {
    setTimeout(initializeCastApi, 1000);
}

function initializeCastApi() {
    var applicationID = chrome.cast.media.DEFAULT_MEDIA_RECEIVER_APP_ID;
    var sessionRequest = new chrome.cast.SessionRequest(applicationID);
    var apiConfig = new chrome.cast.ApiConfig(sessionRequest, sessionListener, receiverListener);

    chrome.cast.initialize(apiConfig, onInitSuccess, onError);
}

/**
 * session listener during initialization
 */
function sessionListener(s) {
    log('New session ID:' + s.sessionId);
    log(s);
    castSession = s;
    setCastControlsVisible(true);
    if (castSession.media.length != 0) {
        log('Found ' + castSession.media.length + ' existing media sessions.');
        onMediaDiscovered('onRequestSessionSuccess_', castSession.media[0]);
    }
    castSession.addMediaListener(onMediaDiscovered.bind(this, 'addMediaListener'));
    castSession.addUpdateListener(sessionUpdateListener.bind(this));
}

/**
 * receiver listener during initialization
 */
function receiverListener(e) {
    if (e === 'available') {
        log("receiver found");
        setImage("castIcon", "<c:url value="/icons/cast/cast_icon_idle.png"/>");
    }
    else {
        log("receiver list empty");
    }
}

/**
 * session update listener
 */
function sessionUpdateListener(isAlive) {
    var message = isAlive ? 'Session Updated' : 'Session Removed';
    message += ': ' + castSession.sessionId;
    log(message);
    if (!isAlive) {
        castSession = null;
        setCastControlsVisible(false);
    }
}

function onInitSuccess() {
    log("init success");
}

function onError() {
    log("error");
}

function setCastControlsVisible(visible) {
    if (visible) {
        $("#flashPlayer").hide();
        $("#castPlayer").show();
        setImage("castIcon", "<c:url value="/icons/cast/cast_icon_active.png"/>");
    } else {
        $("#castPlayer").hide();
        $("#flashPlayer").show();
        setImage("castIcon", "<c:url value="/icons/cast/cast_icon_idle.png"/>");
    }
}

/**
 * launch app and request session
 */
function launchCastApp() {
    log("launching app...");
    chrome.cast.requestSession(onRequestSessionSuccess, onLaunchError);
}

/**
 * callback on success for requestSession call
 * @param {Object} s A non-null new session.
 */
function onRequestSessionSuccess(s) {
    log("session success: " + s.sessionId);
    log(s);
    castSession = s;
    setCastControlsVisible(true);
    castSession.addUpdateListener(sessionUpdateListener.bind(this));
}

function onLaunchError() {
    log("launch error");
}

function loadMedia(song) {
    if (!castSession) {
        log("no session");
        return;
    }
    log("loading..." + song.remoteStreamUrl);
    var mediaInfo = new chrome.cast.media.MediaInfo(song.remoteStreamUrl);
    mediaInfo.contentType = song.contentType;
    mediaInfo.streamType = chrome.cast.media.StreamType.BUFFERED;
    mediaInfo.duration = song.duration;
    mediaInfo.metadata = new chrome.cast.media.MusicTrackMediaMetadata();
    mediaInfo.metadata.metadataType = chrome.cast.media.MetadataType.MUSIC_TRACK;
    mediaInfo.metadata.songName = song.title;
    mediaInfo.metadata.title = song.title;
    mediaInfo.metadata.albumName = song.album;
    mediaInfo.metadata.artist = song.artist;
    mediaInfo.metadata.trackNumber = song.trackNumber;
    mediaInfo.metadata.images = [new chrome.cast.Image(song.remoteCoverArtUrl)];
    mediaInfo.metadata.releaseYear = song.year;

    var request = new chrome.cast.media.LoadRequest(mediaInfo);
    request.autoplay = true;
    request.currentTime = 0;

    castSession.loadMedia(request,
            onMediaDiscovered.bind(this, 'loadMedia'),
            onMediaError);
}

/**
 * callback on success for loading media
 */
function onMediaDiscovered(how, ms) {
    mediaSession = ms;
    log("new media session ID:" + mediaSession.mediaSessionId + ' (' + how + ')');
    log(ms);
    mediaSession.addUpdateListener(onMediaStatusUpdate);
    mediaCurrentTime = mediaSession.currentTime;
}

/**
 * callback on media loading error
 * @param {Object} e A non-null media object
 */
function onMediaError(e) {
    log("media error");
    setImage("castIcon", "<c:url value="/icons/cast/cast_icon_warning.png"/>");
}

/**
 * callback for media status event
 */
function onMediaStatusUpdate(isAlive) {
    log(mediaSession.playerState);
    if (mediaSession.playerState === chrome.cast.media.PlayerState.IDLE && mediaSession.idleReason === "FINISHED") {
        onNext(repeatEnabled);
    }
//    if (progressFlag) {
//        document.getElementById("progress").value = parseInt(100 * mediaSession.currentTime / mediaSession.media.duration);
//    }
//    document.getElementById("playerstate").innerHTML = mediaSession.playerState;
}


function playPauseCast() {
    if (!mediaSession) {
        return;
    }
    if (playing) {
        mediaSession.pause(null, mediaCommandSuccessCallback.bind(this, "paused " + mediaSession.sessionId), onError);
        setImage("castPlayPause", "<spring:theme code="castPlayImage"/>");
    } else {
        mediaSession.play(null, mediaCommandSuccessCallback.bind(this, "playing started for " + mediaSession.sessionId), onError);
        setImage("castPlayPause", "<spring:theme code="castPauseImage"/>");
    }
    playing = !playing;
}

/**
 * set receiver volume
 * @param {Number} level A number for volume level
 * @param {Boolean} mute A true/false for mute/unmute
 */
function setCastVolume(level, mute) {
    if (!castSession)
        return;

    muted = mute;

    if (!mute) {
        castSession.setReceiverVolumeLevel(level, mediaCommandSuccessCallback.bind(this, 'media set-volume done'), onError);
        currentVolume = level;
        setImage("castMute", "<spring:theme code="volumeImage"/>");
    }
    else {
        castSession.setReceiverMuted(true, mediaCommandSuccessCallback.bind(this, 'media set-volume done'), onError);
        setImage("castMute", "<spring:theme code="muteImage"/>");
    }
}

function toggleCastMute() {
    setCastVolume(currentVolume, !muted);
}

/**
 * callback on success for media commands
 * @param {string} info A message string
 */
function mediaCommandSuccessCallback(info) {
    log(info);
}

function setImage(id, image) {
    document.getElementById(id).src = image;
}

function log(message) {
    console.log(message);
    $("#debugmessage").html($("#debugmessage").html() + "\n" + JSON.stringify(message));
}
</script>