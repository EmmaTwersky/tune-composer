## Note:
> When starting this project, our two main goals were to refactor the project to use the model-view-controller concept and to implement the functional requirements of the grouped gestures. We did not realize that the refactoring would be such an extensive process so we decided to return to the original functioning project and implement the Gesture class. Becuase we still believe that the refactoring is important and that the work we did on the MVC_Refactoring branch is important, we would like to submit both the less-elegant functioning solution (unrefactored_add_gesture branch) and the elegant non-functioning solution (MVC_Refactoring).

## Overview of design:
> Our working solution separates classes and controllers although it is not as elegant as we aspire to make this project.
Our MVC_Refactoring solution makes use of the model-view-controller idea. Each class contains the basic information needed to
create the instance (model). Each of those classes has a controller that handles all the modifications and MouseEvents for that
class (controller). Then each of those classes has its own FXML file that handles the visual representation of the members of
the class (view). We believe that this design will allow us to easily add and change classes and functions in the future. We
also made an abstract class SoundObject so that we could have NoteBar and Gesture extend from since they share a lot of
features.

## Elegance:
> We believe that once we complete the MVC_Refactoring solution, it will be very elegant because of the reasons explained
above. Making this disctinction between model and view makes our solution elegant since it makes it more modular and we can
easily add features in the future.

## Inelegance:
> Our working solution is somewhat inelegant because it does not use the model-view-controller idea. We were unable to
implement a funcitoning program that was refactored to this point due to lack of time but we believe that we will be able to
finish this refactoring for the next project and make it very elegant.

## Time Spent:
> 

## Team:
> Our team worked really well on this project. We communicated really well on meeting times and pair-programmed really well
and efficiently too. We also had snacks during our sessions; one day we ordered a pizza and another day we had
Bugles. The snacks kept us motivated and reduced stress.
