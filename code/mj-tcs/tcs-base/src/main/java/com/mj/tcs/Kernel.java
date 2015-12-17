package com.mj.tcs;

import com.mj.tcs.data.ObjectExistsException;
import com.mj.tcs.data.ObjectUnknownException;
import com.mj.tcs.data.base.TCSObjectReference;
import com.mj.tcs.data.base.TCSResourceReference;
import com.mj.tcs.data.model.*;
import com.mj.tcs.data.base.Triple;
import com.mj.tcs.data.order.DriveOrder;
import com.mj.tcs.data.order.OrderSequence;
import com.mj.tcs.data.order.TransportOrder;
import com.mj.tcs.data.user.CredentialsException;
import com.mj.tcs.data.user.UserExistsException;
import com.mj.tcs.data.user.UserPermission;
import com.mj.tcs.data.user.UserUnknownException;
import com.mj.tcs.util.eventsystem.EventSource;
import com.mj.tcs.util.eventsystem.Message;
import com.mj.tcs.util.eventsystem.TcsEvent;
import com.mj.tcs.util.configuration.ConfigurationItem;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Declares the methods the MJTCS kernel must implement which are accessible
 * both to internal components and remote peers (like graphical user
 * interfaces).
 *
 * @author Stefan Walter (Fraunhofer IML)
 */
public interface Kernel extends EventSource<TcsEvent> {

    /**
     * The default name used for the empty model created on startup.
     */
    String DEFAULT_MODEL_NAME = "unnamed";

    /**
     * Returns the permissions the calling client is granted.
     *
     * @return The permissions the calling client is granted.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Set<UserPermission> getUserPermissions()
            throws CredentialsException;

    /**
     * Creates a new user account.
     *
     * @param userName The new user's name.
     * @param userPassword The new user's password.
     * @param userPermissions The new user's permissions.
     * @throws UserExistsException If a user with the given name exists already.
     * @throws UnsupportedKernelOpException If user management is not
     * implemented.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void createUser(String userName, String userPassword,
                    Set<UserPermission> userPermissions)
            throws UserExistsException, UnsupportedKernelOpException,
            CredentialsException;

    /**
     * Changes a user's password.
     *
     * @param userName The name of the user for which the password is to be
     * changed.
     * @param userPassword The user's new password.
     * @throws UserUnknownException If a user with the given name does not exist.
     * @throws UnsupportedKernelOpException If user management is not
     * implemented.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setUserPassword(String userName, String userPassword)
            throws UserUnknownException, UnsupportedKernelOpException,
            CredentialsException;

    /**
     * Changes a user's permissions.
     *
     * @param userName The name of the user for which the permissions are to be
     * changed.
     * @param userPermissions The user's new permissions.
     * @throws UserUnknownException If a user with the given name does not exist.
     * @throws UnsupportedKernelOpException If user management is not
     * implemented.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setUserPermissions(String userName, Set<UserPermission> userPermissions)
            throws UserUnknownException, UnsupportedKernelOpException,
            CredentialsException;

    /**
     * Removes a user account.
     *
     * @param userName The name of the user whose account is to be removed.
     * @throws UserUnknownException If a user with the given name does not exist.
     * @throws UnsupportedKernelOpException If user management is not
     * implemented.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeUser(String userName)
            throws UserUnknownException, UnsupportedKernelOpException,
            CredentialsException;

    /**
     * Returns the current state of the kernel.
     *
     * @return The current state of the kernel.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    State getState()
            throws CredentialsException;

    /**
     * Sets the current state of the kernel.
     *
     * @param newState The state the kernel is to be set to.
     * @throws IllegalArgumentException If setting the new state is not possible,
     * e.g. because a transition from the current to the new state is not allowed.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setState(State newState)
            throws IllegalArgumentException, CredentialsException;

    /**
     * Returns the name of the model that is saved in the kernel.
     *
     * @return The name of the model or <code>null</code> if there is no model.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IOException If getting the model name was not possible.
     */
    String getModelName()
            throws CredentialsException, IOException;

    /**
     * Replaces the kernel's current model with an empty one.
     *
     * @param modelName The newly created model's name.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void createModel(String modelName)
            throws CredentialsException;

    /**
     * Loads the saved model into the kernel.
     * If there is no saved model, a new empty model will be loaded.
     *
     * @throws IOException If the model cannot be loaded.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void loadModel(String newModelName)
            throws IOException, CredentialsException;

    /**
     * Saves the current model under the given name.
     *
     * If there is a saved model, it will be overwritten.
     *
     * @param modelName The name under which the current model is to be saved. If
     * <code>null</code>, the model's current name will be used, otherwise the
     * model will be renamed accordingly.
     * @throws IOException If the model could not be persisted for some reason.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void saveModel(String modelName)
            throws IOException, CredentialsException;

    /**
     * Removes the saved model if there is one.
     *
     * @throws IOException If deleting the model was not possible for some reason.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeModel()
            throws IOException, CredentialsException;

    /**
     * Returns a single TCSObjectReference<?> of the given class.
     *
     * @param <T> The TCSObjectReference<?>'s actual type.
     * @param clazz The class of the object to be returned.
     * @param id The id of the object to be returned.
     * @return A copy of the referenced object, or <code>null</code> if no such
     * object exists or if an object exists but is not an instance of the given
     * class.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    <T extends TCSObjectReference<?>> T getTcsModel(Class<T> clazz,
                                            long id)
            throws CredentialsException;

    /**
     * Returns a single TCSObjectReference<?> of the given class.
     *
     * @param <T> The TCSObjectReference<?>'s actual type.
     * @param clazz The class of the object to be returned.
     * @param name The name of the object to be returned.
     * @return A copy of the named object, or <code>null</code> if no such
     * object exists or if an object exists but is not an instance of the given
     * class.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    <T extends TCSObjectReference<?>> T getTcsModel(Class<T> clazz,
                                            String name)
            throws CredentialsException;

    /**
     * Returns all existing TcsModels of the given class.
     *
     * @param <T> The TcsModels' actual type.
     * @param clazz The class of the objects to be returned.
     * @return Copies of all existing objects of the given class.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    <T extends TCSObjectReference<?>> Set<T> getTcsModels(Class<T> clazz)
            throws CredentialsException;

    /**
     * Returns all existing TcsModels of the given class whose names match the
     * given pattern.
     *
     * @param <T> The TcsModels' actual type.
     * @param clazz The class of the objects to be returned.
     * @param regexp A regular expression describing the names of the objects to
     * be returned; if <code>null</code>, all objects of the given class are
     * returned.
     * @return Copies of all existing objects of the given class whose names match
     * the given pattern. If no such objects exist, the returned set will be
     * empty.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    <T extends TCSObjectReference<?>> Set<T> getTcsModels(Class<T> clazz, Pattern regexp)
            throws CredentialsException;

    /**
     * Rename a TCSObjectReference<?>.
     *
     * @param ref A reference to the object to be renamed.
     * @param newName The object's new name.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws ObjectUnknownException If the referenced object does not exist.
     * @throws ObjectExistsException If the object cannot be renamed because there
     * is already an object with the given new name.
     */
    void renameTcsModel(TCSObjectReference<?> ref, String newName)
            throws CredentialsException, ObjectUnknownException, ObjectExistsException;

    /**
     * Sets an object's property.
     *
     * @param ref A reference to the object to be modified.
     * @param key The property's key.
     * @param value The property's (new) value. If <code>null</code>, removes the
     * property from the object.
     * @throws ObjectUnknownException If the referenced object does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTcsModelProperty(TCSObjectReference<?> ref, String key, String value)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Clears all of an object's properties.
     *
     * @param ref A reference to the object to be modified.
     * @throws ObjectUnknownException If the referenced object does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void clearTcsModelProperties(TCSObjectReference<?> ref)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Remove a TCSObjectReference<?>.
     *
     * @param ref A reference to the object to be removed.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws ObjectUnknownException If the referenced object does not exist.
     */
    void removeTcsModel(TCSObjectReference<?> ref)
            throws CredentialsException, ObjectUnknownException;

    /**
     * Creates a new message with the given content and type.
     *
     * @param message The message's content.
     * @param type The message's type.
     * @return A copy of the newly created message.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Message publishMessage(String message, Message.Type type)
            throws CredentialsException;

//    /**
//     * Adds a visual layout to the current model.
//     * A new layout is created with a unique ID and name and all other attributes
//     * set to default values. A copy of the newly created layout is then returned.
//     *
//     * @return A copy of the newly created layout.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    VisualLayout createVisualLayout()
//            throws CredentialsException;
//
//    /**
//     * Sets a layout's scale on the X axis.
//     *
//     * @param order A reference to the layout to be modified.
//     * @param scaleX The layout's new scale on the X axis.
//     * @throws ObjectUnknownException If the referenced layout does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setVisualLayoutScaleX(VisualLayout order,
//                               double scaleX)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Sets a layout's scale on the Y axis.
//     *
//     * @param order A reference to the layout to be modified.
//     * @param scaleY The layout's new scale on the Y axis.
//     * @throws ObjectUnknownException If the referenced layout does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setVisualLayoutScaleY(VisualLayout order,
//                               double scaleY)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Sets a layout's colors.
//     *
//     * @param order A reference to the layout to be modified.
//     * @param colors The layout's new colors.
//     * @throws ObjectUnknownException If the referenced layout does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setVisualLayoutColors(VisualLayout order,
//                               Map<String, Color> colors)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Sets a layout's elements.
//     *
//     * @param order A reference to the layout to be modified.
//     * @param elements The layout's new elements.
//     * @throws ObjectUnknownException If the referenced layout does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setVisualLayoutElements(VisualLayout order,
//                                 Set<LayoutElement> elements)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Sets a layout's view bookmarks.
//     *
//     * @param order A reference to the layout to be modified.
//     * @param bookmarks The layout's new bookmarks.
//     * @throws ObjectUnknownException If the referenced layout does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void setVisualLayoutViewBookmarks(VisualLayout order,
//                                      List<ViewBookmark> bookmarks)
//            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds a point to the current model.
     * A new point is created with a unique ID and name and all other attributes
     * set to default values. A copy of the newly created point is then returned.
     *
     * @return A copy of the newly created point.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Point createPoint()
            throws CredentialsException;

    /**
     * Sets the physical coordinates of a point.
     *
     * @param point The point to be modified.
     * @param position The point's new coordinates.
     * @throws ObjectUnknownException If the referenced point does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPointPosition(Point point, Triple position)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets the vehicle's (assumed) orientation angle at the given position.
     * The allowed value range is [-360.0..360.0], and <code>Double.NaN</code>, to
     * indicate that the vehicle's orientation at the point is unspecified.
     *
     * @param point A reference to the point to be modified.
     * @param angle The new angle.
     * @throws ObjectUnknownException If the referenced point does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPointVehicleOrientationAngle(Point point,
                                         double angle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets the type of a point.
     *
     * @param point A reference to the point to be modified.
     * @param newType The point's new type.
     * @throws ObjectUnknownException If the referenced point does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPointType(Point point, Point.Type newType)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds a path to the current model.
     * A new path is created with a generated unique ID and name and ending in the
     * point specified by the given name; all other attributes set to default
     * values. Furthermore, the point is registered with the point which it
     * originates in and with the one it ends in. A copy of the newly created path
     * is then returned.
     *
     * @param srcPoint A reference to the point which the newly created path
     * originates in.
     * @param destPoint A reference to the point which the newly created path ends
     * in.
     * @return A copy of the newly created path.
     * @throws ObjectUnknownException If any of the referenced points does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Path createPath(Point srcPoint,
                    Point destPoint)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets the length of a path.
     *
     * @param path A reference to the path to be modified.
     * @param length The new length of the path, in millimetres.
     * @throws ObjectUnknownException If the referenced path does not exist.
     * @throws IllegalArgumentException If <code>length</code> is zero or
     * negative.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPathLength(Path path, long length)
            throws ObjectUnknownException, IllegalArgumentException, CredentialsException;

    /**
     * Sets the routing cost of a path.
     *
     * @param path A reference to the path to be modified.
     * @param cost The new routing cost of the path (unitless).
     * @throws ObjectUnknownException If the referenced path does not exist.
     * @throws IllegalArgumentException If <code>cost</code> is zero or negative.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPathRoutingCost(Path path, long cost)
            throws ObjectUnknownException, IllegalArgumentException, CredentialsException;

    void setPathControlPoints(Path path, List<Triple> ctrlPoints)
            throws ObjectUnknownException, IllegalArgumentException, CredentialsException;

    /**
     * Sets the maximum allowed velocity for a path.
     *
     * @param path A reference to the path to be modified.
     * @param velocity The new maximum allowed velocity in mm/s.
     * @throws ObjectUnknownException If the referenced path does not exist.
     * @throws IllegalArgumentException If <code>velocity</code> is negative.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPathMaxVelocity(Path path, int velocity)
            throws ObjectUnknownException, IllegalArgumentException, CredentialsException;

    /**
     * Sets the maximum allowed reverse velocity for a path.
     *
     * @param path A reference to the path to be modified.
     * @param velocity The new maximum reverse velocity, in mm/s.
     * @throws ObjectUnknownException If the referenced path does not exist.
     * @throws IllegalArgumentException If <code>velocity</code> is negative.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPathMaxReverseVelocity(Path path, int velocity)
            throws ObjectUnknownException, IllegalArgumentException, CredentialsException;

    /**
     * Locks/Unlocks a path.
     *
     * @param path A reference to the path to be modified.
     * @param locked Indicates whether the path is to be locked
     * (<code>true</code>) or unlocked (<code>false</code>).
     * @throws ObjectUnknownException If the referenced path does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setPathLocked(Path path, boolean locked)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new vehicle.
     * A new vehicle is created with a generated unique ID and name and all other
     * attributes set to default values. A copy of the newly created vehicle is
     * then returned.
     *
     * @return A copy of the newly created vehicle type.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Vehicle createVehicle()
            throws CredentialsException;

    /**
     * Sets a vehicle's critical energy level.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param energyLevel The vehicle's new critical energy level.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setVehicleEnergyLevelCritical(Vehicle vehicle,
                                       int energyLevel)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a vehicle's good energy level.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param energyLevel The vehicle's new good energy level.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setVehicleEnergyLevelGood(Vehicle vehicle,
                                   int energyLevel)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a vehicle's length.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param length The vehicle's new length.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setVehicleLength(Vehicle vehicle, int length)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new location type.
     * A new location type is created with a generated unique ID and name and all
     * other attributes set to default values. A copy of the newly created
     * location type is then returned.
     *
     * @return A copy of the newly created location type.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    LocationType createLocationType()
            throws CredentialsException;

    /**
     * Adds an allowed operation to a location type.
     *
     * @param locationType A reference to the location type to be modified.
     * @param operation The operation to be allowed.
     * @throws ObjectUnknownException If the referenced location type does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addLocationTypeAllowedOperation(LocationType locationType,
                                         String operation)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes an allowed operation from a location type.
     *
     * @param locationType A reference to the location type to be modified.
     * @param operation The operation to be disallowed.
     * @throws ObjectUnknownException If the referenced location type does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeLocationTypeAllowedOperation(LocationType locationType,
                                            String operation)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new location.
     * A new location of the given type is created with a generated unique ID and
     * name and all other attributes set to default values. A copy of the newly
     * created location is then returned.
     *
     * @param locationType A reference to the location type of which the newly created
     * location is supposed to be.
     * @return A copy of the newly created location.
     * @throws ObjectUnknownException If the reference location type does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Location createLocation(LocationType locationType)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets the physical coordinates of a location.
     *
     * @param location A reference to the location to be modified.
     * @param position The location's new coordinates.
     * @throws ObjectUnknownException If the referenced location does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setLocationPosition(Location location, Triple position)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a location's type.
     *
     * @param location A reference to the location.
     * @param locationType A reference to the location's new type.
     * @throws ObjectUnknownException If the referenced location or type do not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setLocationType(Location location,
                         LocationType locationType)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Connects a location to a point (expressing that the location is reachable
     * from that point).
     *
     * @param location A reference to the location.
     * @param point A reference to the point.
     * @throws ObjectUnknownException If the referenced location or point does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void connectLocationToPoint(Location location,
                                Point point)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Disconnects a location from a point (expressing that the location isn't
     * reachable from the point any more).
     *
     * @param location A reference to the location.
     * @param point A reference to the point.
     * @throws ObjectUnknownException If the referenced location or point does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void disconnectLocationFromPoint(Location location,
                                     Point point)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds an allowed operation to a link between a location and a point.
     *
     * @param location A reference to the location end of the link to be modified.
     * @param point A reference to the point end of the link to be modified.
     * @param operation The operation to be added.
     * @throws ObjectUnknownException If any of the referenced objects does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addLocationLinkAllowedOperation(Location location,
                                         Point point,
                                         String operation)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes an allowed operation from a link between a location and a point.
     *
     * @param location A reference to the location end of the link to be modified.
     * @param point A reference to the point end of the link to be modified.
     * @param operation The operation to be removed.
     * @throws ObjectUnknownException If any of the referenced objects does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeLocationLinkAllowedOperation(Location location,
                                            Point point,
                                            String operation)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes all allowed operations (for all vehicle types) from a link between
     * a location and a point.
     *
     * @param location A reference to the location end of the link to be modified.
     * @param point A reference to the point end of the link to be modified.
     * @throws ObjectUnknownException If any of the referenced objects does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void clearLocationLinkAllowedOperations(Location location,
                                            Point point)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds a block to the current model.
     * A new block is created with a unique ID and name and all other attributes
     * set to default values. A copy of the newly created block is then returned.
     *
     * @return A copy of the newly created block.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Block createBlock()
            throws CredentialsException;

    /**
     * Adds a member to a block.
     *
     * @param block A reference to the block to be modified.
     * @param newMember A reference to the new member.
     * @throws ObjectUnknownException If any of the referenced block and member do
     * not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addBlockMember(Block block,
                        TCSResourceReference<?> newMember)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes a member from a block.
     *
     * @param block A reference to the block to be modified.
     * @param rmMember A reference to the member to be removed.
     * @throws ObjectUnknownException If the referenced block does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeBlockMember(Block block,
                           TCSResourceReference<?> rmMember)
            throws ObjectUnknownException, CredentialsException;

//    /**
//     * Adds a group to the current model.
//     * A new group is created with a unique ID and name and all other attributes
//     * set to default values. A copy of the newly created block is then returned.
//     *
//     * @return A copy of the newly created group.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    Group createGroup()
//            throws CredentialsException;
//
//    /**
//     * Adds a member to a group.
//     *
//     * @param order A reference to the group to be modified.
//     * @param newMemberRef A reference to the new member.
//     * @throws ObjectUnknownException If any of the referenced group and member do
//     * not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void addGroupMember(Group order,
//                        TCSObjectReference<?> newMemberRef)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Removes a member from a group.
//     *
//     * @param order A reference to the group to be modified.
//     * @param rmMemberRef A reference to the member to be removed.
//     * @throws ObjectUnknownException If the referenced group does not exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void removeGroupMember(Group order,
//                           TCSObjectReference<?> rmMemberRef)
//            throws ObjectUnknownException, CredentialsException;

    /**
     * Adds a static route to the current model.
     * A new route is created with a unique ID and name and all other attributes
     * set to default values. A copy of the newly created route is then returned.
     *
     * @return A copy of the newly created route.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    StaticRoute createStaticRoute()
            throws CredentialsException;

    /**
     * Adds a hop to a route.
     *
     * @param staticRoute A reference to the route to be modified.
     * @param point A reference to the new hop.
     * @throws ObjectUnknownException If any of the referenced route and hop do
     * not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addStaticRouteHop(StaticRoute staticRoute,
                           Point point)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes all hops from a route.
     *
     * @param staticRoute A reference to the route to be modified.
     * @throws ObjectUnknownException If the referenced route does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void clearStaticRouteHops(StaticRoute staticRoute)
            throws ObjectUnknownException, CredentialsException;

//    /**
//     * Attaches a resource to another one.
//     *
//     * @param resource A reference to the resource that is to receive the
//     * attachment.
//     * @param newResource A reference to the resource to be attached.
//     * @throws ObjectUnknownException If any of the referenced resources does not
//     * exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void attachResource(TCSObjectReference<?> resource,
//                        TCSObjectReference<?> newResource)
//            throws ObjectUnknownException, CredentialsException;
//
//    /**
//     * Detaches a resource from another one.
//     *
//     * @param resource A reference to the resource from which the attached
//     * resource is to be removed.
//     * @param rmResource A reference to the resource to be detached.
//     * @throws ObjectUnknownException If any of the referenced resources does not
//     * exist.
//     * @throws CredentialsException If the calling client is not allowed to
//     * execute this method.
//     */
//    void detachResource(TCSObjectReference<?> resource,
//                        TCSObjectReference<?> rmResource)
//            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new transport order.
     * A new transport order is created with a generated unique ID and name,
     * containing the given <code>DriveOrder</code>s and with all other attributes
     * set to their default values. A copy of the newly created transport order
     * is then returned.
     *
     * @param destinations The list of destinations that have to be travelled to
     * when processing this transport order.
     * @return A copy of the newly created transport order.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    TransportOrder createTransportOrder(List<DriveOrder.Destination> destinations)
            throws CredentialsException;

    /**
     * Sets a transport order's deadline.
     *
     * @param order The transport order to be modified.
     * @param deadline The transport order's new deadline.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderDeadline(TransportOrder order,
                                   long deadline)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Activates a transport order and makes it available for processing by a
     * vehicle.
     *
     * @param order The transport order to be modified.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void activateTransportOrder(TransportOrder order)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets a transport order's intended vehicle.
     *
     * @param order The transport order to be modified.
     * @param vehicle The vehicle intended to process the order.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setTransportOrderIntendedVehicle(
            TransportOrder order,
            Vehicle vehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Copies drive order data from a list of drive orders to the given transport
     * order's future drive orders.
     *
     * @param order The transport order to be modified.
     * @param newOrders The drive orders containing the data to be copied into
     * this transport order's drive orders.
     * @throws ObjectUnknownException If the referenced transport order is not
     * in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the destinations of the given drive
     * orders do not match the destinations of the drive orders in this transport
     * order.
     */
    void setTransportOrderFutureDriveOrders(
            TransportOrder order,
            List<DriveOrder> newOrders)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Adds a dependency to a transport order on another transport order.
     *
     * @param order The order that the dependency is to be added
     * to.
     * @param newDep The order that is the new dependency.
     * @throws ObjectUnknownException If any of the referenced transport orders
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void addTransportOrderDependency(TransportOrder order,
                                     TransportOrder newDep)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Removes a dependency from a transport order on another transport order.
     *
     * @param order The order that the dependency is to be
     * removed from.
     * @param rmDep the order that is not to be depended on any
     * more.
     * @throws ObjectUnknownException If any of the referenced transport orders
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeTransportOrderDependency(
            TransportOrder order,
            TransportOrder rmDep)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates a new order sequence.
     * A new order sequence is created with a generated unique ID and name. A copy
     * of the newly created order sequence is then returned.
     *
     * @return A copy of the newly created order sequence.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    OrderSequence createOrderSequence()
            throws CredentialsException;

    /**
     * Adds a transport order to an order sequence.
     *
     * @param seq The order sequence to be modified.
     * @param order The transport order to be added.
     * @throws ObjectUnknownException If the referenced order sequence or
     * transport order is not in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the sequence is already marked as
     * <em>complete</em>, if the sequence already contains the given order or
     * if the given transport order has already been activated.
     */
    void addOrderSequenceOrder(OrderSequence seq,
                               TransportOrder order)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Removes a transport order from an order sequence.
     *
     * @param seq The order sequence to be modified.
     * @param order The transport order to be removed.
     * @throws ObjectUnknownException If the referenced order sequence or
     * transport order is not in this pool.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void removeOrderSequenceOrder(OrderSequence seq,
                                  TransportOrder order)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's complete flag.
     *
     * @param seq The order sequence to be modified.
     * @throws ObjectUnknownException If the referenced order sequence does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceComplete(OrderSequence seq)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's <em>failureFatal</em> flag.
     *
     * @param seq The order sequence to be modified.
     * @param fatal The sequence's new <em>failureFatal</em> flag.
     * @throws ObjectUnknownException If the referenced order sequence does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceFailureFatal(OrderSequence seq,
                                      boolean fatal)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Sets an order sequence's intended vehicle.
     *
     * @param seq The order sequence to be modified.
     * @param vehicle The vehicle intended to process the order
     * sequence.
     * @throws ObjectUnknownException If the referenced order sequence or vehicle
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setOrderSequenceIntendedVehicle(OrderSequence seq,
                                         Vehicle vehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Withdraw the referenced order, set its state to FAILED and stop the vehicle
     * that might be processing it.
     * Calling this method once will initiate the withdrawal, leaving the
     * transport order assigned to the vehicle until it has finished the movements
     * that it has already been ordered to execute. The transport order's state
     * will change to WITHDRAWN. Calling this method a second time for the same
     * vehicle/order will withdraw the order from the vehicle without further
     * waiting.
     *
     * @param order The transport order to be withdrawn.
     * @param disableVehicle Whether setting the processing state of the vehicle
     * currently processing the transport order to
     * {@link com.mj.tcs.data.model.Vehicle.ProcState#UNAVAILABLE} to prevent
     * immediate redispatching of the vehicle.
     * @throws ObjectUnknownException If the referenced transport order does not
     * exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void withdrawTransportOrder(TransportOrder order,
                                boolean disableVehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Withdraw any order that a vehicle might be processing, set its state to
     * FAILED and stop the vehicle.
     * Calling this method once will initiate the withdrawal, leaving the
     * transport order assigned to the vehicle until it has finished the movements
     * that it has already been ordered to execute. The transport order's state
     * will change to WITHDRAWN. Calling this method a second time for the same
     * vehicle/order will withdraw the order from the vehicle without further
     * waiting.
     *
     * @param vehicle A reference to the vehicle to be modified.
     * @param disableVehicle Whether setting the processing state of the vehicle
     * currently processing the transport order to
     * {@link com.mj.tcs.data.model.Vehicle.ProcState#UNAVAILABLE} to prevent
     * immediate redispatching of the vehicle.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void withdrawTransportOrderByVehicle(Vehicle vehicle,
                                         boolean disableVehicle)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Explicitly trigger dispatching of the referenced idle vehicle.
     *
     * @param vehicle A reference to the vehicle to be dispatched.
     * @param setIdleIfUnavailable Whether to set the vehicle's processing state
     * to IDLE before dispatching if it is currently UNAVAILABLE. If the vehicle's
     * processing state is UNAVAILABLE and this flag is not set, an
     * IllegalArgumentException will be thrown.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IllegalArgumentException If the referenced vehicle is not in a
     * dispatchable state (IDLE or, if the corresponding flag is set, UNAVAILABLE).
     */
    void dispatchVehicle(Vehicle vehicle,
                         boolean setIdleIfUnavailable)
            throws ObjectUnknownException, CredentialsException, IllegalArgumentException;

    /**
     * Sends a message to the communication adapter associated with the referenced
     * vehicle.
     * This method provides a generic one-way communication channel to the
     * communication adapter of a vehicle. Note that there is no return value and
     * no guarantee that the communication adapter will understand the message;
     * clients cannot even know which communication adapter is attached to a
     * vehicle, so it's entirely possible that the communication adapter receiving
     * the message does not understand it.
     *
     * @param vehicle The vehicle whose communication adapter shall receive the
     * message.
     * @param message The message to be delivered.
     * @throws ObjectUnknownException If the referenced vehicle does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void sendCommAdapterMessage(Vehicle vehicle,
                                Object message)
            throws ObjectUnknownException, CredentialsException;

    /**
     * Creates and returns a list of transport orders defined in a script file.
     *
     * @param fileName The name of the script file defining the transport orders
     * to be created.
     * @return The list of transport orders created. If none were created, the
     * returned list is empty.
     * @throws ObjectUnknownException If any object referenced in the script file
     * does not exist.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws IOException If there was a problem reading or parsing the file with
     * the given name.
     */
//    List<TransportOrder> createTransportOrdersFromScript(String fileName)
//            throws ObjectUnknownException, CredentialsException, IOException;

    /**
     * Returns the costs for travelling from one location to a given set of
     * others.
     *
     * @param vehicle The vehicle that shall be used for calculating
     * the costs. If it's <code>null</code> a random vehicle will be used.
     * @param srcLocation The source Location
     * @param destinations A set containing all destination locations
     * @return A list containing tuples of a location and the costs to travel there
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     * @throws ObjectUnknownException If something is not known.
     */
    List<TravelCosts> getTravelCosts(Vehicle vehicle,
                                                   Location srcLocation,
                                                   Set<Location> destinations)
            throws CredentialsException, ObjectUnknownException;

    /**
     * Returns the result of the query defined by the given class.
     *
     * @param <T> The result's actual type.
     * @param clazz Defines the query and the class of the result to be returned.
     * @return The result of the query defined by the given class, or
     * <code>null</code>, if the defined query is not supported in the kernel's
     * current state.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
//    <T extends Query<T>> T query(Class<T> clazz)
//            throws CredentialsException;

    /**
     * Returns the current time factor for simulation.
     *
     * @return The current time factor for simulation.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    double getSimulationTimeFactor()
            throws CredentialsException;

    /**
     * Sets a time factor for simulation.
     *
     * @param factor The new time factor.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setSimulationTimeFactor(double factor)
            throws CredentialsException;

    /**
     * Returns all configuration items existing in the kernel.
     *
     * @return All configuration items existing in the kernel.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    Set<ConfigurationItem> getConfigurationItems()
            throws CredentialsException;

    /**
     * Sets a single configuration item in the kernel.
     *
     * @param itemTO The configuration item to be set.
     * @throws CredentialsException If the calling client is not allowed to
     * execute this method.
     */
    void setConfigurationItem(ConfigurationItem itemTO)
            throws CredentialsException;

    /**
     * The various states a kernel instance may be running in.
     */
    public enum State {

        /**
         * The state in which the model/topology is created and parameters are set.
         */
        MODELLING("MODELLING"),
        /**
         * The normal mode of operation in which transport orders may be accepted
         * and dispatched to vehicles.
         */
        OPERATING("OPERATING"),
        /**
         * A transitional state the kernel is in while shutting down.
         */
        SHUTDOWN("SHUTDOWN");

        private String text;

        State(String text) {
            this.text = text;
        }

        @Override
        public String toString() {
            return this.text;
        }

        public static State fromString(String text) {
            Optional<State> type = Arrays.stream(State.values())
                    .filter(s -> s.toString().compareToIgnoreCase(text) == 0).findFirst();

            if (type.isPresent()) {
                return type.get();
            }

            throw new KernelRuntimeException("The Kernel.State enum type is no recognizable [text=" + text + "]");
        }
    }
}
