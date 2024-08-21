package br.com.saggion.scrcpytools.controller

import br.com.saggion.scrcpytools.util.ADB
import br.com.saggion.scrcpytools.util.Alert
import br.com.saggion.scrcpytools.util.DataHolder
import javafx.application.Platform
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.scene.control.Alert.AlertType
import javafx.scene.control.Button
import javafx.scene.control.ListView
import javafx.stage.FileChooser
import java.io.File
import java.net.URL
import java.util.ResourceBundle
import kotlin.concurrent.thread

class TaskManager : Initializable {
    @FXML
    lateinit var listViewPackages: ListView<String>

    @FXML
    lateinit var buttonInstall: Button

    @FXML
    lateinit var buttonStart: Button

    @FXML
    lateinit var buttonForceStop: Button

    @FXML
    lateinit var buttonClearData: Button

    @FXML
    lateinit var buttonUninstall: Button

    @FXML
    lateinit var buttonReload: Button

    override fun initialize(
        location: URL?,
        resources: ResourceBundle?,
    ) {
        loadPackages()
    }

    private fun loadPackages() {
        val packages = ADB.listPackages(DataHolder.instance.device).sorted()
        listViewPackages.items.clear()
        listViewPackages.items.addAll(packages)
    }

    @FXML
    fun buttonStartOnAction() {
        if (!isPackageSelected()) {
            Alert(AlertType.WARNING, "Select a package to perform this action").show()
            return
        }
        val packageName = listViewPackages.selectionModel.selectedItem
        ADB.start(DataHolder.instance.device, packageName)
    }

    @FXML
    fun buttonForceStopOnAction() {
        if (!isPackageSelected()) {
            Alert(AlertType.WARNING, "Select a package to perform this action").show()
            return
        }
        val packageName = listViewPackages.selectionModel.selectedItem
        ADB.forceStop(DataHolder.instance.device, packageName)
    }

    @FXML
    fun buttonClearDataOnAction() {
        if (!isPackageSelected()) {
            Alert(AlertType.WARNING, "Select a package to perform this action").show()
            return
        }
        val packageName = listViewPackages.selectionModel.selectedItem
        ADB.clearData(DataHolder.instance.device, packageName)
    }

    @FXML
    fun buttonUninstallOnAction() {
        if (!isPackageSelected()) {
            Alert(AlertType.WARNING, "Select a package to perform this action").show()
            return
        }
        val packageName = listViewPackages.selectionModel.selectedItem
        ADB.uninstall(DataHolder.instance.device, packageName)
        loadPackages()
    }

    @FXML
    fun buttonInstallOnAction() {
        val fileChooser =
            FileChooser().apply {
                title = "Select the apk file"
                initialDirectory = File(File("").absolutePath)
                initialFileName = ""
                extensionFilters.add(FileChooser.ExtensionFilter("APK files", "*.apk"))
            }
        val file = fileChooser.showOpenDialog(buttonInstall.scene.window) ?: return

        val pathToApk =
            if (file.absolutePath.lowercase().endsWith(".apk")) {
                file.absolutePath
            } else {
                file.absolutePath + ".apk"
            }
        lockControls(true)
        thread {
            val installSucceded = ADB.install(DataHolder.instance.device, pathToApk)
            lockControls(false)
            if (installSucceded) {
                Platform.runLater {
                    loadPackages()
                    Alert(AlertType.INFORMATION, "APK installed successfully").show()
                }
            } else {
                Platform.runLater { Alert(AlertType.ERROR, "Failed to install the apk").show() }
            }
        }
    }

    private fun lockControls(lock: Boolean) {
        buttonInstall.isDisable = lock
        buttonStart.isDisable = lock
        buttonForceStop.isDisable = lock
        buttonClearData.isDisable = lock
        buttonUninstall.isDisable = lock
        buttonReload.isDisable = lock
        listViewPackages.isDisable = lock
    }

    @FXML
    fun buttonReloadOnAction() {
        loadPackages()
    }

    private fun isPackageSelected(): Boolean = listViewPackages.selectionModel.selectedItem != null
}
