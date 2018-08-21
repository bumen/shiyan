import static java.lang.System.getProperty;

import cognitivej.vision.computervision.ComputerVisionScenario;
import cognitivej.vision.computervision.ImageDescription;
import cognitivej.vision.overlay.builder.ImageOverlayBuilder;

public class ImageCognitiveTest {

    private static final String  IMAGE_URL ="https://camo.githubusercontent.com/e75252a1f4dcacb1db1f8ad802602af3648c5c58/68747470733a2f2f69776b656c6c792e66696c65732e776f726470726573732e636f6d2f323031362f30352f73637265656e2d73686f742d323031362d30352d31312d61742d31372d31322d34392e706e67";



    public static void main(String[] args) {
        ComputerVisionScenario computerVisionScenario = new ComputerVisionScenario("f8cdef31-a31e-4b4a-93e4-5f571e91255a");
        ImageDescription imageDescription = computerVisionScenario.describeImage(IMAGE_URL);
        ImageOverlayBuilder.builder(IMAGE_URL).describeImage(imageDescription).launchViewer();
    }
}
