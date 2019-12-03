package sample;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Main extends Application {
    private TextField tfProxyIP;
    private TextField tfProxyPort;
    private TextField tfTargetServer;
    private Button btnOk;
    private TextArea taResult;

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        tfProxyIP = (TextField) root.lookup("#proxyIP");
        tfProxyPort = (TextField) root.lookup("#proxyPort");
        tfTargetServer = (TextField) root.lookup("#targetServer");
        btnOk = (Button) root.lookup("#ok");
        taResult = (TextArea) root.lookup("#result");

        btnOk.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                taResult.setText(ProxyTest.beginTest(tfProxyIP.getText(), tfProxyPort.getText(), tfTargetServer.getText()));
            }
        });
        primaryStage.setTitle("代理访问测试程序");
        primaryStage.setScene(new Scene(root, 600, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
