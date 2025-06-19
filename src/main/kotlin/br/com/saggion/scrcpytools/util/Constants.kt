package br.com.saggion.scrcpytools.util

class Constants {
    companion object {
        const val APP_TITLE = "Screen Copy Tools"
        const val SCRCPY_VERSION = "v3.3"
        const val SCRCPY_WIN64_ZIP = "scrcpy-win64-$SCRCPY_VERSION.zip"
        const val SCRCPY_FOLDER = "scrcpy-win64-$SCRCPY_VERSION"
        const val BASE_PACKAGE = "/br/com/saggion/scrcpytools"
        val TEMP_DIRECTORY: String = System.getProperty("java.io.tmpdir")
        val ADB_PATH = "${this.TEMP_DIRECTORY}scrcpy-tools/${SCRCPY_FOLDER}/adb.exe"
        val SCRCPY_PATH = "${TEMP_DIRECTORY}scrcpy-tools/${SCRCPY_FOLDER}/scrcpy.exe"
    }
}
