package com.mj.tcs.data.model;

import com.mj.tcs.data.base.BaseEntity;
import com.mj.tcs.data.base.Triple;

import javax.persistence.*;
import java.util.*;

/**
 * A location at which a vehicle may perform an action.
 * <ul>
 * <li>A <code>Location</code> must be linked to at least one <code>Point</code>
 * to be reachable for a vehicle.</li>
 * <li>It may be linked to multiple <code>Point</code>s.</li>
 * <li>As long as a link's specific set of allowed operations is empty (which is
 * the default), all operations defined by the <code>Location</code>'s
 * referenced <code>LocationTypeDto</code> are allowed at the linked
 * <code>Point</code>. If the link's set of allowed operations is not empty,
 * only the operations contained in it are allowed at the linked
 * <code>Point</code>.</li>
 * </ul>
 *
 * @author Wang Zhen
 */
@Entity
@Table(name = "tcs_model_location", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
)
public class Location extends BaseLocation {
    @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
    @JoinColumn(name = "scene", nullable = false)
    private Scene scene;

    /**
     * This location's position in mm.
     */
    @OneToOne(optional = false, cascade = {CascadeType.ALL})
//    @JoinColumn(name = "position_id")
    private Triple position = new Triple();

    /**
     * A set of links attached to this location.
     */
    @OneToMany(mappedBy = "location", cascade = {CascadeType.ALL})
    private Set<Link> attachedLinks = new HashSet<>();

    public Location(){
    }
    /**
     * Creates a new Location.
     *
     * @param name The new location's name.
     * @param locationType The new location's type.
     */
    public Location(String name,
                    LocationType locationType) {
        setName(name);
        setType(locationType);
    }

    @Override
    public void clearId() {
        setId(null);
        if (getPosition() != null) {
            getPosition().clearId();
        }
        if (getAttachedLinks() != null) {
            getAttachedLinks().forEach(l -> l.clearId());
        }
        // Location type should NOT be changed.
//        if (getType() != null) {
//            getType().clearId();
//        }
    }

    public Scene getScene() {
        return scene;
    }

    public void setScene(Scene scene) {
        this.scene = scene;
    }

    /**
     * Returns the physical coordinates of this location in mm.
     *
     * @return The physical coordinates of this location in mm.
     */
    public Triple getPosition() {
      return position;
    }

    /**
     * Sets the physical coordinates of this location in mm.
     *
     * @param newPosition The new physical coordinates of this location. May not
     * be <code>null</code>.
     */
    public void setPosition(Triple newPosition) {
      position = Objects.requireNonNull(newPosition, "newPosition is null");
    }

    /**
     * Returns a set of links attached to this location.
     *
     * @return A set of links attached to this location.
     */
    public Set<Link> getAttachedLinks() {
      return attachedLinks;
    }

    public void setAttachedLinks(Set<Link> attachedLinks) {
        this.attachedLinks = attachedLinks;
    }

    public Optional<Link> getAttachedLinkById(long id) {
        if (this.attachedLinks == null) {
            return Optional.ofNullable(null);
        }

        return attachedLinks.stream().filter(l -> l.getId() == id).findFirst();
    }

    /**
     * Attaches a link to this location.
     *
     * @param newLink The link to be attached to this location.
     * @return <code>true</code> if, and only if, the given link was not already
     * attached to this location.
     * @throws IllegalArgumentException If the location end of the given link is
     * not this location.
     */
    public boolean attachLink(Link newLink) {
        Objects.requireNonNull(newLink, "newLink is null");
        Location linkLocation = Objects.requireNonNull(newLink.getLocation(), "newLink's location is null");

        if (!linkLocation.equals(this)) {
            throw new IllegalArgumentException(
                    "location end of link is not this location");
        }
        return attachedLinks.add(newLink);
    }

    /**
     * Detaches a link from this location.
     *
     * @param point The point end of the link to be detached from this
     * location.
     * @return <code>true</code> if, and only if, there was a link to the given
     * point attached to this location.
     */
    public boolean detachLink(Point point) {
      Objects.requireNonNull(point, "point is null");
      Iterator<Link> linkIter = attachedLinks.iterator();
      while (linkIter.hasNext()) {
          Link curLink = linkIter.next();
        if (point.equals(curLink.getPoint())) {
          linkIter.remove();
          return true;
        }
      }
      return false;
    }

    /**
     * Adds an allowed operation to a link between a location and a point.
     *
     * @param point
     * @param operation
     */
    public void addLocationLinkAllowedOperation(Point point, String operation) {
        Objects.requireNonNull(point, "point is null");
        Objects.requireNonNull(operation, "operation is null");

        Optional<Link> referredLink = attachedLinks.stream()
                .filter(l -> point.equals(l.getPoint())).findFirst();
        if (!referredLink.isPresent()) {
            throw new NullPointerException("Described link is not in this model");
        }

        referredLink.get().addAllowedOperation(operation);
    }

    /**
     * Removes an allowed operation from a link between a location and a point.
     *
     * @param point
     * @param operation
     */
    public void removeLocationLinkAllowedOperation(Point point, String operation) {
        Objects.requireNonNull(point, "point is null");
        Objects.requireNonNull(operation, "operation is null");

        Optional<Link> referredLink = attachedLinks.stream()
                .filter(l -> point.equals(l.getPoint())).findFirst();
        if (!referredLink.isPresent()) {
            throw new NullPointerException("Described link not in this model");
        }

        referredLink.get().removeAllowedOperation(operation);
    }

    /**
     * Removes all allowed operations (for all vehicle types) from a link between
     * a location and a point.
     *
     * @param point
     */
    public void clearLocationLinkAllowedOperations(Point point) {
        Objects.requireNonNull(point, "point is null");

        Optional<Link> referredLink = attachedLinks.stream()
                .filter(l -> point.equals(l.getPoint())).findFirst();
        if (!referredLink.isPresent()) {
            throw new NullPointerException("Described link not in this model");
        }

        referredLink.get().clearAllowedOperations();
    }

    @Override
    public Location clone() {
        Location clone = null;
        clone = (Location) super.clone();
        clone.position = (position == null) ? null : position;
        clone.type = type.clone();
        clone.attachedLinks = new HashSet<>();
        if (attachedLinks != null) {
            for (Link curLink : attachedLinks) {
                clone.attachedLinks.add(curLink.clone());
            }
        }

        return clone;
    }

    @Entity
    @Table(name = "tcs_model_location_link", uniqueConstraints =
    @UniqueConstraint(columnNames = {"name", "scene"})
    )
    public static class Link extends BaseEntity implements Cloneable {

        @ManyToOne(cascade = {CascadeType.DETACH, CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REFRESH})
        @JoinColumn(name = "scene", nullable = false)
        private Scene scene;

        /**
         * A reference to the location end of this link.
         */
        @ManyToOne
        private Location location;
        /**
         * A reference to the point end of this link.
         */
        @ManyToOne
        private Point point;
        /**
         * The operations allowed at this link.
         */
        // divided by ;
        private String allowedOperations = "";

        public Link() {
        }

        /**
         * Clear its ID only!
         */
        @Override
        public void clearId() {
            setId(null);
        }

        public Scene getScene() {
            return scene;
        }

        public void setScene(Scene scene) {
            this.scene = scene;
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public void setPoint(Point point) {
            this.point = point;
        }

        public void setAllowedOperations(Set<String> allowedOperations) {
            if (allowedOperations == null || allowedOperations.size() == 0) {
                this.allowedOperations = "";
            }

            final  StringBuffer buffer = new StringBuffer();
            for (String op : allowedOperations) {
                if (op == null || op.trim().isEmpty()) {
                    continue;
                }

                if (buffer.length() != 0) {
                    buffer.append(";");
                }

                buffer.append(op.trim());
            }

            this.allowedOperations = buffer.toString();
        }

        /**
         * Creates a new Link.
         *
         * @param linkLocation A reference to the location end of this link.
         * @param linkPoint A reference to the point end of this link.
         */
        public Link(Location linkLocation, Point linkPoint) {
            location = Objects.requireNonNull(linkLocation, "linkLocation is null");
            point = Objects.requireNonNull(linkPoint, "linkPoint is null");
        }

        /**
         * Returns a reference to the location end of this link.
         *
         * @return A reference to the location end of this link.
         */
        public Location getLocation() {
            return location;
        }

        /**
         * Returns a reference to the point end of this link.
         *
         * @return A reference to the point end of this link.
         */
        public Point getPoint() {
            return point;
        }

        /**
         * Returns the operations allowed at this link.
         *
         * @return The operations allowed at this link.
         */
        public Set<String> getAllowedOperations() {
            return stringToSet(allowedOperations);
        }

        /**
         * Checks if a vehicle is allowed to execute a given operation at this link.
         *
         * @param operation The operation to be checked.
         * @return <code>true</code> if, and only if, vehicles are allowed to
         * execute the given operation at his link.
         */
        public boolean hasAllowedOperation(String operation) {
            Objects.requireNonNull(operation, "operation is null");
            Set<String> ops = getAllowedOperations();
            if (ops == null) {
                return false;
            }

            return ops.contains(operation);
        }

        /**
         * Removes all allowed operations from this link.
         */
        public void clearAllowedOperations() {
            allowedOperations = "";
        }

        /**
         * Adds an allowed operation.
         *
         * @param operation The operation to be allowed.
         * @return <code>true</code> if, and only if, the given operation wasn't
         * already allowed before.
         */
        public boolean addAllowedOperation(String operation) {
            Objects.requireNonNull(operation, "operation is null");
            Set<String> ops = getAllowedOperations();
            if (ops == null) {
                return false;
            }

            boolean answer = ops.add(operation);

            allowedOperations = setToString(ops);

            return answer;
        }

        /**
         * Removes an allowed operation.
         *
         * @param operation The operation to be disallowed.
         * @return <code>true</code> if, and only if, the given operation was
         * allowed before.
         */
        public boolean removeAllowedOperation(String operation) {
            Objects.requireNonNull(operation, "operation is null");
            Set<String> ops = getAllowedOperations();
            if (ops == null) {
                return true;
            }

            boolean answer = ops.remove(operation);

            allowedOperations = setToString(ops);
            return answer;
        }

        /**
         * Checks if this object is equal to another one.
         * Two <code>Link</code>s are equal if they both reference the same location
         * and point ends.
         *
         * @param obj The object to compare this one to.
         * @return <code>true</code> if, and only if, <code>obj</code> is also a
         * <code>Link</code> and reference the same location and point ends as this
         * one.
         */
        @Override
        public boolean equals(Object obj) {
            if (obj instanceof Link) {
                Link other = (Link) obj;
                return point.equals(other.getPoint())
                        && location.equals(other.getLocation());
            }
            else {
                return false;
            }
        }

        private String setToString(Set<String> strList) {
            if (strList == null || strList.size() == 0) {
                return "";
            }

            StringBuffer buffer = new StringBuffer();
            // FORMAT: xxx;yyy;zzz
            for (String str : strList) {
                if (str == null || str.trim().length() == 0) {
                    continue;
                }

                if(buffer.length() != 0) { // not the first time
                    buffer.append(";");
                }
                buffer.append(str.trim());
            }

            return buffer.toString();
        }

        private Set<String> stringToSet(String inString) {
            if (inString == null || inString.trim().length() == 0) {
                return new TreeSet<>();
            }

            String[] texts = inString.trim().split(";");
            if (texts == null || texts.length == 0) {
                return new TreeSet<>();
            }

            Set<String> answer = new TreeSet<>();
            for (String str : texts) {
                if (str == null || str.length() == 0) {
                    continue;
                }

                answer.add(str);
            }
            return answer;
        }

        @Override
        public Link clone() {
            Link clone;
            clone = (Link) super.clone();
            clone.location = location.clone();
            clone.point = point.clone();
            clone.allowedOperations = setToString(getAllowedOperations());
            return clone;
        }
    }
}
