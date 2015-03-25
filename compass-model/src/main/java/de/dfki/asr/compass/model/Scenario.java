/*
 * This file is part of COMPASS. It is subject to the license terms in
 * the LICENSE file found in the top-level directory of this distribution.
 * (Also available at http://www.apache.org/licenses/LICENSE-2.0.txt)
 * You may not use this file except in compliance with the License.
 */
package de.dfki.asr.compass.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import de.dfki.asr.compass.model.resource.Image;
import java.io.Serializable;
import java.util.Comparator;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import org.eclipse.persistence.oxm.annotations.XmlInverseReference;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@Entity
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@JsonAutoDetect(fieldVisibility = Visibility.NONE,getterVisibility = Visibility.NONE,setterVisibility = Visibility.NONE)
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "project_id", "name" }))
@ApiModel //the mixins change the data references, so that only ids or lists of ids will be delivered
public class Scenario extends AbstractCompassEntity implements Serializable {

	private static final long serialVersionUID = 1226510924828828152L;

	@Id
	@XmlTransient
	@JsonProperty
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty("Id")
	private long id;

	@NotNull(message = "Scenario must have a name.")
	@XmlElement
	@JsonProperty
	@Column(length = 128)
	@Size(min = 1, max = 128, message = "Maximum Prefab Set name length is ${max} characters.")
	@Pattern(regexp = "^(?!\\s)[-\\w :.()]+(?<!\\s)$", message = "Scenario name can only contain letters, dashes, braces, colons and dots. No leading or trailing spaces.")
	@ApiModelProperty("Name")
	private String name;

	@XmlElement
	@JsonProperty
	@OneToOne(cascade = CascadeType.ALL)
	@ApiModelProperty(value = "Preview image id", dataType = "long")
	private Image preview;

	@NotNull(message = "Scenario must have a root scene node.")
	@XmlElement
	@JsonProperty
	@OneToOne(cascade = CascadeType.ALL)
	@ApiModelProperty(value = "Id of the root scene node", dataType = "long")
	private SceneNode root;

	@NotNull(message = "Scenario must reference a project.")
	@ManyToOne
	@JsonProperty
	@XmlInverseReference(mappedBy = "scenarios")
	@JoinColumn(name = "project_id")
	@ApiModelProperty(value = "Id of the parent project", dataType = "long")
	private Project project;

	public Scenario() {
		name = "";
		root = new SceneNode("Root");
	}

	@Override
	public long getId() {
		return id;
	}

	@Override
	public void setId(final long id) {
		this.id = id;
	}

	@Override
	public void forceEagerFetch() {
		root.forceEagerFetch();
		if (preview != null) {
			preview.forceEagerFetch();
		}
	}

	@Override
	public void clearIdsAfterDeepCopy() {
		setId(0);
		if (root != null) {
			root.clearIdsAfterDeepCopy();
		}
		if (preview != null) {
			preview.clearIdsAfterDeepCopy();
		}
	}

	public String getName() {
		return name;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public Image getPreview() {
		return preview;
	}

	public void setPreview(final Image preview) {
		this.preview = preview;
	}

	public SceneNode getRoot() {
		return root;
	}

	public void setRoot(final SceneNode root) {
		this.root = root;
	}

	public void setProject(final Project project) {
		if (this.project == project) {
			return;
		}

		Project oldProject = this.project;
		this.project = project;

		if (oldProject != null) {
			oldProject.removeScenario(this);
		}

		if (project != null) {
			try {
				project.addScenario(this);
			} catch (IllegalArgumentException e) {
				throw new IllegalStateException("Should never reach this point.", e);
			}
		}
	}

	public Project getProject() {
		return project;
	}

	@Override
	public String toString() {
		return "Scenario [id=" + id + "]" + " (" + name + ")";
	}

	public static class Comparators {

		public static final Comparator<Scenario> NAME = new Comparator<Scenario>() {

			@Override
			public int compare(final Scenario o1, final Scenario o2) {
				return o1.name.toLowerCase().compareTo(o2.name.toLowerCase());
			}
		};
	}

}
