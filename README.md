# WebView Instagram App

A minimal Kotlin based Android app that loads Instagram in a WebView, keeps the user logged in, and automatically force-closes after 15 minutes of use. It then requres a 2-minute cooldown period before allowing the app to re-launch, with ProGuard/R8 minification for a smaller APK.

## High-Level Overview
This project exemplifies **end-to-end Android proficiency** by combining:
- A **single-activity** architecture in Kotlin.
- Seamless **WebView integration** with persistent **Instagram sessions**.
- **Lifecycle management**: forced close after 15 minutes, with a 2-minute cooldown enforced on relaunch.
- **Performance optimization** through **ProGuard/R8** code and resource shrinking, yielding a minimalist APK size.

## Project Structure
- **MainActivity.kt**:  
  Implements the WebView, time-limited session logic, and preference checks.  
- **activity_main.xml**:  
  Hosts a single `<WebView>` component.  
- **AndroidManifest.xml**:  
  Declares needed permissions (Internet) and sets the Activity.

## Setup & Build
1. **Clone or Download** this repository.  
2. **Open** in Android Studio:  
3. Ensure **Kotlin** support and **API 28** SDK are installed.  
4. **Build & Run**

## Usage Flow
1. **Launch the App**  
   - Loads [instagram.com](https://instagram.com) in a WebView.  
   - Log in once; session cookies persist for future launches.

2. **Active Usage**  
   - A 15-minute timer commences on launch.  
   - The app **auto-closes** upon timer expiration, storing a closure timestamp.

3. **Cooldown**  
   - Attempting to reopen within 2 minutes results in immediate closure.  
   - After 2 minutes, usage is restored.

## Customization
- **Time Constraints**: Modify `USE_TIME_MINUTES` and `COOL_DOWN_MINUTES` in `MainActivity.kt`.  
- **Target URL**: Change the loaded URL in `webView.loadUrl(...)`.  

## Security & Privacy
- **Session Security**: Cookies reside on the device; unauthorized access to the device can compromise the session.  
- **Forced Closure**: Non-standard in typical Android apps—validate policy and user acceptance if distributing publicly.
