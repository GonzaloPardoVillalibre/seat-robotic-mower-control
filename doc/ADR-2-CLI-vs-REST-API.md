
# ADR 2. Command line interface vs REST API

## OBJECTIVE
Determine which approach is better to solve the tech assessment.

## DISCUSSION
The decision between developing a Command Line Interface (CLI) application and a REST API depends on the expected usage, integration needs, and scalability of the application.

### CLI Application
- **Simplicity**: Straightforward to develop and can be easily run on any system with minimal setup.
- **Direct Control**: For operations staff at the factory who might be interacting directly with the system on a daily basis, a CLI could provide a quick way to issue commands and get immediate feedback without needing a network infrastructure.
- Limited to Single User Operations.


### REST API 
- **Integration and Scalability**: A REST API can serve multiple clients over a network and can be integrated with other systems (like IoT devices, other enterprise applications at SEAT, mobile apps for monitoring, etc.).
- **Remote Access**: Can be accessed remotely and can serve multiple requests simultaneously. This could be useful for integrating the mower control system with other automated systems within the factory or for remote monitoring and control.
- **Complexity and Maintenance**: Developing a REST API involves more complexity, including handling network issues, securing the API, and possibly developing client applications (web or mobile) to interact with the API.
Recommendations

## FINAL DECISSION

We will develop a REST API to manage and control the robotic mowers at the SEAT Martorell Factory. The decision Drivers were:
- **Scalability**: The project, although in its MVP stage, is positioned to scale significantly. A REST API can effectively manage increased load from more mowers and larger areas without significant changes to the underlying architecture.

- **Integration Capability** with third apps.

- **Remote Access and Control**.

- **Future Proofing**: The REST architecture is widely supported and understood, making it easier to maintain and update the system as technology evolves and new features are required.


Given the strategic importance of the project, its potential to scale, and the need for integration with other technological systems within the SEAT factory, a REST API seems like a good choice. This approach ensures that the mower control system is robust, flexible, and future-ready.