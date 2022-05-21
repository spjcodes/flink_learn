/**
 * Autogenerated by Avro
 *
 * DO NOT EDIT DIRECTLY
 */
package cn.jiayeli.movieAnalyse.module;

import org.apache.avro.specific.SpecificData;
import org.apache.avro.message.BinaryMessageEncoder;
import org.apache.avro.message.BinaryMessageDecoder;
import org.apache.avro.message.SchemaStore;

@SuppressWarnings("all")
@org.apache.avro.specific.AvroGenerated
public class UserMovieRatingInfoModule extends org.apache.avro.specific.SpecificRecordBase implements org.apache.avro.specific.SpecificRecord {
  private static final long serialVersionUID = 1222733345910003385L;
  public static final org.apache.avro.Schema SCHEMA$ = new org.apache.avro.Schema.Parser().parse("{\"type\":\"record\",\"name\":\"UserMovieRatingInfoModule\",\"namespace\":\"cn.jiayeli.movieAnalyse.module\",\"fields\":[{\"name\":\"userId\",\"type\":\"string\"},{\"name\":\"age\",\"type\":\"string\"},{\"name\":\"gender\",\"type\":\"string\"},{\"name\":\"occupation\",\"type\":\"string\"},{\"name\":\"zipCode\",\"type\":\"string\"},{\"name\":\"movieId\",\"type\":\"string\"},{\"name\":\"movieTitle\",\"type\":\"string\"},{\"name\":\"releaseDate\",\"type\":\"string\"},{\"name\":\"videoReleaseDate\",\"type\":\"string\"},{\"name\":\"IMDbURL\",\"type\":\"string\"},{\"name\":\"type\",\"type\":\"string\"},{\"name\":\"rating\",\"type\":\"int\"},{\"name\":\"timestamp\",\"type\":\"string\"}]}");
  public static org.apache.avro.Schema getClassSchema() { return SCHEMA$; }

  private static SpecificData MODEL$ = new SpecificData();

  private static final BinaryMessageEncoder<UserMovieRatingInfoModule> ENCODER =
      new BinaryMessageEncoder<UserMovieRatingInfoModule>(MODEL$, SCHEMA$);

  private static final BinaryMessageDecoder<UserMovieRatingInfoModule> DECODER =
      new BinaryMessageDecoder<UserMovieRatingInfoModule>(MODEL$, SCHEMA$);

  /**
   * Return the BinaryMessageDecoder instance used by this class.
   */
  public static BinaryMessageDecoder<UserMovieRatingInfoModule> getDecoder() {
    return DECODER;
  }

  /**
   * Create a new BinaryMessageDecoder instance for this class that uses the specified {@link SchemaStore}.
   * @param resolver a {@link SchemaStore} used to find schemas by fingerprint
   */
  public static BinaryMessageDecoder<UserMovieRatingInfoModule> createDecoder(SchemaStore resolver) {
    return new BinaryMessageDecoder<UserMovieRatingInfoModule>(MODEL$, SCHEMA$, resolver);
  }

  /** Serializes this UserMovieRatingInfoModule to a ByteBuffer. */
  public java.nio.ByteBuffer toByteBuffer() throws java.io.IOException {
    return ENCODER.encode(this);
  }

  /** Deserializes a UserMovieRatingInfoModule from a ByteBuffer. */
  public static UserMovieRatingInfoModule fromByteBuffer(
      java.nio.ByteBuffer b) throws java.io.IOException {
    return DECODER.decode(b);
  }

  @Deprecated public java.lang.CharSequence userId;
  @Deprecated public java.lang.CharSequence age;
  @Deprecated public java.lang.CharSequence gender;
  @Deprecated public java.lang.CharSequence occupation;
  @Deprecated public java.lang.CharSequence zipCode;
  @Deprecated public java.lang.CharSequence movieId;
  @Deprecated public java.lang.CharSequence movieTitle;
  @Deprecated public java.lang.CharSequence releaseDate;
  @Deprecated public java.lang.CharSequence videoReleaseDate;
  @Deprecated public java.lang.CharSequence IMDbURL;
  @Deprecated public java.lang.CharSequence type;
  @Deprecated public int rating;
  @Deprecated public java.lang.CharSequence timestamp;

  /**
   * Default constructor.  Note that this does not initialize fields
   * to their default values from the schema.  If that is desired then
   * one should use <code>newBuilder()</code>.
   */
  public UserMovieRatingInfoModule() {}

  /**
   * All-args constructor.
   * @param userId The new value for userId
   * @param age The new value for age
   * @param gender The new value for gender
   * @param occupation The new value for occupation
   * @param zipCode The new value for zipCode
   * @param movieId The new value for movieId
   * @param movieTitle The new value for movieTitle
   * @param releaseDate The new value for releaseDate
   * @param videoReleaseDate The new value for videoReleaseDate
   * @param IMDbURL The new value for IMDbURL
   * @param type The new value for type
   * @param rating The new value for rating
   * @param timestamp The new value for timestamp
   */
  public UserMovieRatingInfoModule(java.lang.CharSequence userId, java.lang.CharSequence age, java.lang.CharSequence gender, java.lang.CharSequence occupation, java.lang.CharSequence zipCode, java.lang.CharSequence movieId, java.lang.CharSequence movieTitle, java.lang.CharSequence releaseDate, java.lang.CharSequence videoReleaseDate, java.lang.CharSequence IMDbURL, java.lang.CharSequence type, java.lang.Integer rating, java.lang.CharSequence timestamp) {
    this.userId = userId;
    this.age = age;
    this.gender = gender;
    this.occupation = occupation;
    this.zipCode = zipCode;
    this.movieId = movieId;
    this.movieTitle = movieTitle;
    this.releaseDate = releaseDate;
    this.videoReleaseDate = videoReleaseDate;
    this.IMDbURL = IMDbURL;
    this.type = type;
    this.rating = rating;
    this.timestamp = timestamp;
  }

  public org.apache.avro.Schema getSchema() { return SCHEMA$; }
  // Used by DatumWriter.  Applications should not call.
  public java.lang.Object get(int field$) {
    switch (field$) {
    case 0: return userId;
    case 1: return age;
    case 2: return gender;
    case 3: return occupation;
    case 4: return zipCode;
    case 5: return movieId;
    case 6: return movieTitle;
    case 7: return releaseDate;
    case 8: return videoReleaseDate;
    case 9: return IMDbURL;
    case 10: return type;
    case 11: return rating;
    case 12: return timestamp;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  // Used by DatumReader.  Applications should not call.
  @SuppressWarnings(value="unchecked")
  public void put(int field$, java.lang.Object value$) {
    switch (field$) {
    case 0: userId = (java.lang.CharSequence)value$; break;
    case 1: age = (java.lang.CharSequence)value$; break;
    case 2: gender = (java.lang.CharSequence)value$; break;
    case 3: occupation = (java.lang.CharSequence)value$; break;
    case 4: zipCode = (java.lang.CharSequence)value$; break;
    case 5: movieId = (java.lang.CharSequence)value$; break;
    case 6: movieTitle = (java.lang.CharSequence)value$; break;
    case 7: releaseDate = (java.lang.CharSequence)value$; break;
    case 8: videoReleaseDate = (java.lang.CharSequence)value$; break;
    case 9: IMDbURL = (java.lang.CharSequence)value$; break;
    case 10: type = (java.lang.CharSequence)value$; break;
    case 11: rating = (java.lang.Integer)value$; break;
    case 12: timestamp = (java.lang.CharSequence)value$; break;
    default: throw new org.apache.avro.AvroRuntimeException("Bad index");
    }
  }

  /**
   * Gets the value of the 'userId' field.
   * @return The value of the 'userId' field.
   */
  public java.lang.CharSequence getUserId() {
    return userId;
  }

  /**
   * Sets the value of the 'userId' field.
   * @param value the value to set.
   */
  public void setUserId(java.lang.CharSequence value) {
    this.userId = value;
  }

  /**
   * Gets the value of the 'age' field.
   * @return The value of the 'age' field.
   */
  public java.lang.CharSequence getAge() {
    return age;
  }

  /**
   * Sets the value of the 'age' field.
   * @param value the value to set.
   */
  public void setAge(java.lang.CharSequence value) {
    this.age = value;
  }

  /**
   * Gets the value of the 'gender' field.
   * @return The value of the 'gender' field.
   */
  public java.lang.CharSequence getGender() {
    return gender;
  }

  /**
   * Sets the value of the 'gender' field.
   * @param value the value to set.
   */
  public void setGender(java.lang.CharSequence value) {
    this.gender = value;
  }

  /**
   * Gets the value of the 'occupation' field.
   * @return The value of the 'occupation' field.
   */
  public java.lang.CharSequence getOccupation() {
    return occupation;
  }

  /**
   * Sets the value of the 'occupation' field.
   * @param value the value to set.
   */
  public void setOccupation(java.lang.CharSequence value) {
    this.occupation = value;
  }

  /**
   * Gets the value of the 'zipCode' field.
   * @return The value of the 'zipCode' field.
   */
  public java.lang.CharSequence getZipCode() {
    return zipCode;
  }

  /**
   * Sets the value of the 'zipCode' field.
   * @param value the value to set.
   */
  public void setZipCode(java.lang.CharSequence value) {
    this.zipCode = value;
  }

  /**
   * Gets the value of the 'movieId' field.
   * @return The value of the 'movieId' field.
   */
  public java.lang.CharSequence getMovieId() {
    return movieId;
  }

  /**
   * Sets the value of the 'movieId' field.
   * @param value the value to set.
   */
  public void setMovieId(java.lang.CharSequence value) {
    this.movieId = value;
  }

  /**
   * Gets the value of the 'movieTitle' field.
   * @return The value of the 'movieTitle' field.
   */
  public java.lang.CharSequence getMovieTitle() {
    return movieTitle;
  }

  /**
   * Sets the value of the 'movieTitle' field.
   * @param value the value to set.
   */
  public void setMovieTitle(java.lang.CharSequence value) {
    this.movieTitle = value;
  }

  /**
   * Gets the value of the 'releaseDate' field.
   * @return The value of the 'releaseDate' field.
   */
  public java.lang.CharSequence getReleaseDate() {
    return releaseDate;
  }

  /**
   * Sets the value of the 'releaseDate' field.
   * @param value the value to set.
   */
  public void setReleaseDate(java.lang.CharSequence value) {
    this.releaseDate = value;
  }

  /**
   * Gets the value of the 'videoReleaseDate' field.
   * @return The value of the 'videoReleaseDate' field.
   */
  public java.lang.CharSequence getVideoReleaseDate() {
    return videoReleaseDate;
  }

  /**
   * Sets the value of the 'videoReleaseDate' field.
   * @param value the value to set.
   */
  public void setVideoReleaseDate(java.lang.CharSequence value) {
    this.videoReleaseDate = value;
  }

  /**
   * Gets the value of the 'IMDbURL' field.
   * @return The value of the 'IMDbURL' field.
   */
  public java.lang.CharSequence getIMDbURL() {
    return IMDbURL;
  }

  /**
   * Sets the value of the 'IMDbURL' field.
   * @param value the value to set.
   */
  public void setIMDbURL(java.lang.CharSequence value) {
    this.IMDbURL = value;
  }

  /**
   * Gets the value of the 'type' field.
   * @return The value of the 'type' field.
   */
  public java.lang.CharSequence getType() {
    return type;
  }

  /**
   * Sets the value of the 'type' field.
   * @param value the value to set.
   */
  public void setType(java.lang.CharSequence value) {
    this.type = value;
  }

  /**
   * Gets the value of the 'rating' field.
   * @return The value of the 'rating' field.
   */
  public java.lang.Integer getRating() {
    return rating;
  }

  /**
   * Sets the value of the 'rating' field.
   * @param value the value to set.
   */
  public void setRating(java.lang.Integer value) {
    this.rating = value;
  }

  /**
   * Gets the value of the 'timestamp' field.
   * @return The value of the 'timestamp' field.
   */
  public java.lang.CharSequence getTimestamp() {
    return timestamp;
  }

  /**
   * Sets the value of the 'timestamp' field.
   * @param value the value to set.
   */
  public void setTimestamp(java.lang.CharSequence value) {
    this.timestamp = value;
  }

  /**
   * Creates a new UserMovieRatingInfoModule RecordBuilder.
   * @return A new UserMovieRatingInfoModule RecordBuilder
   */
  public static cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder newBuilder() {
    return new cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder();
  }

  /**
   * Creates a new UserMovieRatingInfoModule RecordBuilder by copying an existing Builder.
   * @param other The existing builder to copy.
   * @return A new UserMovieRatingInfoModule RecordBuilder
   */
  public static cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder newBuilder(cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder other) {
    return new cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder(other);
  }

  /**
   * Creates a new UserMovieRatingInfoModule RecordBuilder by copying an existing UserMovieRatingInfoModule instance.
   * @param other The existing instance to copy.
   * @return A new UserMovieRatingInfoModule RecordBuilder
   */
  public static cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder newBuilder(cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule other) {
    return new cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder(other);
  }

  /**
   * RecordBuilder for UserMovieRatingInfoModule instances.
   */
  public static class Builder extends org.apache.avro.specific.SpecificRecordBuilderBase<UserMovieRatingInfoModule>
    implements org.apache.avro.data.RecordBuilder<UserMovieRatingInfoModule> {

    private java.lang.CharSequence userId;
    private java.lang.CharSequence age;
    private java.lang.CharSequence gender;
    private java.lang.CharSequence occupation;
    private java.lang.CharSequence zipCode;
    private java.lang.CharSequence movieId;
    private java.lang.CharSequence movieTitle;
    private java.lang.CharSequence releaseDate;
    private java.lang.CharSequence videoReleaseDate;
    private java.lang.CharSequence IMDbURL;
    private java.lang.CharSequence type;
    private int rating;
    private java.lang.CharSequence timestamp;

    /** Creates a new Builder */
    private Builder() {
      super(SCHEMA$);
    }

    /**
     * Creates a Builder by copying an existing Builder.
     * @param other The existing Builder to copy.
     */
    private Builder(cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder other) {
      super(other);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.age)) {
        this.age = data().deepCopy(fields()[1].schema(), other.age);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.gender)) {
        this.gender = data().deepCopy(fields()[2].schema(), other.gender);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.occupation)) {
        this.occupation = data().deepCopy(fields()[3].schema(), other.occupation);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.zipCode)) {
        this.zipCode = data().deepCopy(fields()[4].schema(), other.zipCode);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.movieId)) {
        this.movieId = data().deepCopy(fields()[5].schema(), other.movieId);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.movieTitle)) {
        this.movieTitle = data().deepCopy(fields()[6].schema(), other.movieTitle);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.releaseDate)) {
        this.releaseDate = data().deepCopy(fields()[7].schema(), other.releaseDate);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.videoReleaseDate)) {
        this.videoReleaseDate = data().deepCopy(fields()[8].schema(), other.videoReleaseDate);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.IMDbURL)) {
        this.IMDbURL = data().deepCopy(fields()[9].schema(), other.IMDbURL);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.type)) {
        this.type = data().deepCopy(fields()[10].schema(), other.type);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.rating)) {
        this.rating = data().deepCopy(fields()[11].schema(), other.rating);
        fieldSetFlags()[11] = true;
      }
      if (isValidValue(fields()[12], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[12].schema(), other.timestamp);
        fieldSetFlags()[12] = true;
      }
    }

    /**
     * Creates a Builder by copying an existing UserMovieRatingInfoModule instance
     * @param other The existing instance to copy.
     */
    private Builder(cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule other) {
            super(SCHEMA$);
      if (isValidValue(fields()[0], other.userId)) {
        this.userId = data().deepCopy(fields()[0].schema(), other.userId);
        fieldSetFlags()[0] = true;
      }
      if (isValidValue(fields()[1], other.age)) {
        this.age = data().deepCopy(fields()[1].schema(), other.age);
        fieldSetFlags()[1] = true;
      }
      if (isValidValue(fields()[2], other.gender)) {
        this.gender = data().deepCopy(fields()[2].schema(), other.gender);
        fieldSetFlags()[2] = true;
      }
      if (isValidValue(fields()[3], other.occupation)) {
        this.occupation = data().deepCopy(fields()[3].schema(), other.occupation);
        fieldSetFlags()[3] = true;
      }
      if (isValidValue(fields()[4], other.zipCode)) {
        this.zipCode = data().deepCopy(fields()[4].schema(), other.zipCode);
        fieldSetFlags()[4] = true;
      }
      if (isValidValue(fields()[5], other.movieId)) {
        this.movieId = data().deepCopy(fields()[5].schema(), other.movieId);
        fieldSetFlags()[5] = true;
      }
      if (isValidValue(fields()[6], other.movieTitle)) {
        this.movieTitle = data().deepCopy(fields()[6].schema(), other.movieTitle);
        fieldSetFlags()[6] = true;
      }
      if (isValidValue(fields()[7], other.releaseDate)) {
        this.releaseDate = data().deepCopy(fields()[7].schema(), other.releaseDate);
        fieldSetFlags()[7] = true;
      }
      if (isValidValue(fields()[8], other.videoReleaseDate)) {
        this.videoReleaseDate = data().deepCopy(fields()[8].schema(), other.videoReleaseDate);
        fieldSetFlags()[8] = true;
      }
      if (isValidValue(fields()[9], other.IMDbURL)) {
        this.IMDbURL = data().deepCopy(fields()[9].schema(), other.IMDbURL);
        fieldSetFlags()[9] = true;
      }
      if (isValidValue(fields()[10], other.type)) {
        this.type = data().deepCopy(fields()[10].schema(), other.type);
        fieldSetFlags()[10] = true;
      }
      if (isValidValue(fields()[11], other.rating)) {
        this.rating = data().deepCopy(fields()[11].schema(), other.rating);
        fieldSetFlags()[11] = true;
      }
      if (isValidValue(fields()[12], other.timestamp)) {
        this.timestamp = data().deepCopy(fields()[12].schema(), other.timestamp);
        fieldSetFlags()[12] = true;
      }
    }

    /**
      * Gets the value of the 'userId' field.
      * @return The value.
      */
    public java.lang.CharSequence getUserId() {
      return userId;
    }

    /**
      * Sets the value of the 'userId' field.
      * @param value The value of 'userId'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setUserId(java.lang.CharSequence value) {
      validate(fields()[0], value);
      this.userId = value;
      fieldSetFlags()[0] = true;
      return this;
    }

    /**
      * Checks whether the 'userId' field has been set.
      * @return True if the 'userId' field has been set, false otherwise.
      */
    public boolean hasUserId() {
      return fieldSetFlags()[0];
    }


    /**
      * Clears the value of the 'userId' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearUserId() {
      userId = null;
      fieldSetFlags()[0] = false;
      return this;
    }

    /**
      * Gets the value of the 'age' field.
      * @return The value.
      */
    public java.lang.CharSequence getAge() {
      return age;
    }

    /**
      * Sets the value of the 'age' field.
      * @param value The value of 'age'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setAge(java.lang.CharSequence value) {
      validate(fields()[1], value);
      this.age = value;
      fieldSetFlags()[1] = true;
      return this;
    }

    /**
      * Checks whether the 'age' field has been set.
      * @return True if the 'age' field has been set, false otherwise.
      */
    public boolean hasAge() {
      return fieldSetFlags()[1];
    }


    /**
      * Clears the value of the 'age' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearAge() {
      age = null;
      fieldSetFlags()[1] = false;
      return this;
    }

    /**
      * Gets the value of the 'gender' field.
      * @return The value.
      */
    public java.lang.CharSequence getGender() {
      return gender;
    }

    /**
      * Sets the value of the 'gender' field.
      * @param value The value of 'gender'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setGender(java.lang.CharSequence value) {
      validate(fields()[2], value);
      this.gender = value;
      fieldSetFlags()[2] = true;
      return this;
    }

    /**
      * Checks whether the 'gender' field has been set.
      * @return True if the 'gender' field has been set, false otherwise.
      */
    public boolean hasGender() {
      return fieldSetFlags()[2];
    }


    /**
      * Clears the value of the 'gender' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearGender() {
      gender = null;
      fieldSetFlags()[2] = false;
      return this;
    }

    /**
      * Gets the value of the 'occupation' field.
      * @return The value.
      */
    public java.lang.CharSequence getOccupation() {
      return occupation;
    }

    /**
      * Sets the value of the 'occupation' field.
      * @param value The value of 'occupation'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setOccupation(java.lang.CharSequence value) {
      validate(fields()[3], value);
      this.occupation = value;
      fieldSetFlags()[3] = true;
      return this;
    }

    /**
      * Checks whether the 'occupation' field has been set.
      * @return True if the 'occupation' field has been set, false otherwise.
      */
    public boolean hasOccupation() {
      return fieldSetFlags()[3];
    }


    /**
      * Clears the value of the 'occupation' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearOccupation() {
      occupation = null;
      fieldSetFlags()[3] = false;
      return this;
    }

    /**
      * Gets the value of the 'zipCode' field.
      * @return The value.
      */
    public java.lang.CharSequence getZipCode() {
      return zipCode;
    }

    /**
      * Sets the value of the 'zipCode' field.
      * @param value The value of 'zipCode'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setZipCode(java.lang.CharSequence value) {
      validate(fields()[4], value);
      this.zipCode = value;
      fieldSetFlags()[4] = true;
      return this;
    }

    /**
      * Checks whether the 'zipCode' field has been set.
      * @return True if the 'zipCode' field has been set, false otherwise.
      */
    public boolean hasZipCode() {
      return fieldSetFlags()[4];
    }


    /**
      * Clears the value of the 'zipCode' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearZipCode() {
      zipCode = null;
      fieldSetFlags()[4] = false;
      return this;
    }

    /**
      * Gets the value of the 'movieId' field.
      * @return The value.
      */
    public java.lang.CharSequence getMovieId() {
      return movieId;
    }

    /**
      * Sets the value of the 'movieId' field.
      * @param value The value of 'movieId'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setMovieId(java.lang.CharSequence value) {
      validate(fields()[5], value);
      this.movieId = value;
      fieldSetFlags()[5] = true;
      return this;
    }

    /**
      * Checks whether the 'movieId' field has been set.
      * @return True if the 'movieId' field has been set, false otherwise.
      */
    public boolean hasMovieId() {
      return fieldSetFlags()[5];
    }


    /**
      * Clears the value of the 'movieId' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearMovieId() {
      movieId = null;
      fieldSetFlags()[5] = false;
      return this;
    }

    /**
      * Gets the value of the 'movieTitle' field.
      * @return The value.
      */
    public java.lang.CharSequence getMovieTitle() {
      return movieTitle;
    }

    /**
      * Sets the value of the 'movieTitle' field.
      * @param value The value of 'movieTitle'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setMovieTitle(java.lang.CharSequence value) {
      validate(fields()[6], value);
      this.movieTitle = value;
      fieldSetFlags()[6] = true;
      return this;
    }

    /**
      * Checks whether the 'movieTitle' field has been set.
      * @return True if the 'movieTitle' field has been set, false otherwise.
      */
    public boolean hasMovieTitle() {
      return fieldSetFlags()[6];
    }


    /**
      * Clears the value of the 'movieTitle' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearMovieTitle() {
      movieTitle = null;
      fieldSetFlags()[6] = false;
      return this;
    }

    /**
      * Gets the value of the 'releaseDate' field.
      * @return The value.
      */
    public java.lang.CharSequence getReleaseDate() {
      return releaseDate;
    }

    /**
      * Sets the value of the 'releaseDate' field.
      * @param value The value of 'releaseDate'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setReleaseDate(java.lang.CharSequence value) {
      validate(fields()[7], value);
      this.releaseDate = value;
      fieldSetFlags()[7] = true;
      return this;
    }

    /**
      * Checks whether the 'releaseDate' field has been set.
      * @return True if the 'releaseDate' field has been set, false otherwise.
      */
    public boolean hasReleaseDate() {
      return fieldSetFlags()[7];
    }


    /**
      * Clears the value of the 'releaseDate' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearReleaseDate() {
      releaseDate = null;
      fieldSetFlags()[7] = false;
      return this;
    }

    /**
      * Gets the value of the 'videoReleaseDate' field.
      * @return The value.
      */
    public java.lang.CharSequence getVideoReleaseDate() {
      return videoReleaseDate;
    }

    /**
      * Sets the value of the 'videoReleaseDate' field.
      * @param value The value of 'videoReleaseDate'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setVideoReleaseDate(java.lang.CharSequence value) {
      validate(fields()[8], value);
      this.videoReleaseDate = value;
      fieldSetFlags()[8] = true;
      return this;
    }

    /**
      * Checks whether the 'videoReleaseDate' field has been set.
      * @return True if the 'videoReleaseDate' field has been set, false otherwise.
      */
    public boolean hasVideoReleaseDate() {
      return fieldSetFlags()[8];
    }


    /**
      * Clears the value of the 'videoReleaseDate' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearVideoReleaseDate() {
      videoReleaseDate = null;
      fieldSetFlags()[8] = false;
      return this;
    }

    /**
      * Gets the value of the 'IMDbURL' field.
      * @return The value.
      */
    public java.lang.CharSequence getIMDbURL() {
      return IMDbURL;
    }

    /**
      * Sets the value of the 'IMDbURL' field.
      * @param value The value of 'IMDbURL'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setIMDbURL(java.lang.CharSequence value) {
      validate(fields()[9], value);
      this.IMDbURL = value;
      fieldSetFlags()[9] = true;
      return this;
    }

    /**
      * Checks whether the 'IMDbURL' field has been set.
      * @return True if the 'IMDbURL' field has been set, false otherwise.
      */
    public boolean hasIMDbURL() {
      return fieldSetFlags()[9];
    }


    /**
      * Clears the value of the 'IMDbURL' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearIMDbURL() {
      IMDbURL = null;
      fieldSetFlags()[9] = false;
      return this;
    }

    /**
      * Gets the value of the 'type' field.
      * @return The value.
      */
    public java.lang.CharSequence getType() {
      return type;
    }

    /**
      * Sets the value of the 'type' field.
      * @param value The value of 'type'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setType(java.lang.CharSequence value) {
      validate(fields()[10], value);
      this.type = value;
      fieldSetFlags()[10] = true;
      return this;
    }

    /**
      * Checks whether the 'type' field has been set.
      * @return True if the 'type' field has been set, false otherwise.
      */
    public boolean hasType() {
      return fieldSetFlags()[10];
    }


    /**
      * Clears the value of the 'type' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearType() {
      type = null;
      fieldSetFlags()[10] = false;
      return this;
    }

    /**
      * Gets the value of the 'rating' field.
      * @return The value.
      */
    public java.lang.Integer getRating() {
      return rating;
    }

    /**
      * Sets the value of the 'rating' field.
      * @param value The value of 'rating'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setRating(int value) {
      validate(fields()[11], value);
      this.rating = value;
      fieldSetFlags()[11] = true;
      return this;
    }

    /**
      * Checks whether the 'rating' field has been set.
      * @return True if the 'rating' field has been set, false otherwise.
      */
    public boolean hasRating() {
      return fieldSetFlags()[11];
    }


    /**
      * Clears the value of the 'rating' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearRating() {
      fieldSetFlags()[11] = false;
      return this;
    }

    /**
      * Gets the value of the 'timestamp' field.
      * @return The value.
      */
    public java.lang.CharSequence getTimestamp() {
      return timestamp;
    }

    /**
      * Sets the value of the 'timestamp' field.
      * @param value The value of 'timestamp'.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder setTimestamp(java.lang.CharSequence value) {
      validate(fields()[12], value);
      this.timestamp = value;
      fieldSetFlags()[12] = true;
      return this;
    }

    /**
      * Checks whether the 'timestamp' field has been set.
      * @return True if the 'timestamp' field has been set, false otherwise.
      */
    public boolean hasTimestamp() {
      return fieldSetFlags()[12];
    }


    /**
      * Clears the value of the 'timestamp' field.
      * @return This builder.
      */
    public cn.jiayeli.movieAnalyse.module.UserMovieRatingInfoModule.Builder clearTimestamp() {
      timestamp = null;
      fieldSetFlags()[12] = false;
      return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public UserMovieRatingInfoModule build() {
      try {
        UserMovieRatingInfoModule record = new UserMovieRatingInfoModule();
        record.userId = fieldSetFlags()[0] ? this.userId : (java.lang.CharSequence) defaultValue(fields()[0]);
        record.age = fieldSetFlags()[1] ? this.age : (java.lang.CharSequence) defaultValue(fields()[1]);
        record.gender = fieldSetFlags()[2] ? this.gender : (java.lang.CharSequence) defaultValue(fields()[2]);
        record.occupation = fieldSetFlags()[3] ? this.occupation : (java.lang.CharSequence) defaultValue(fields()[3]);
        record.zipCode = fieldSetFlags()[4] ? this.zipCode : (java.lang.CharSequence) defaultValue(fields()[4]);
        record.movieId = fieldSetFlags()[5] ? this.movieId : (java.lang.CharSequence) defaultValue(fields()[5]);
        record.movieTitle = fieldSetFlags()[6] ? this.movieTitle : (java.lang.CharSequence) defaultValue(fields()[6]);
        record.releaseDate = fieldSetFlags()[7] ? this.releaseDate : (java.lang.CharSequence) defaultValue(fields()[7]);
        record.videoReleaseDate = fieldSetFlags()[8] ? this.videoReleaseDate : (java.lang.CharSequence) defaultValue(fields()[8]);
        record.IMDbURL = fieldSetFlags()[9] ? this.IMDbURL : (java.lang.CharSequence) defaultValue(fields()[9]);
        record.type = fieldSetFlags()[10] ? this.type : (java.lang.CharSequence) defaultValue(fields()[10]);
        record.rating = fieldSetFlags()[11] ? this.rating : (java.lang.Integer) defaultValue(fields()[11]);
        record.timestamp = fieldSetFlags()[12] ? this.timestamp : (java.lang.CharSequence) defaultValue(fields()[12]);
        return record;
      } catch (java.lang.Exception e) {
        throw new org.apache.avro.AvroRuntimeException(e);
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumWriter<UserMovieRatingInfoModule>
    WRITER$ = (org.apache.avro.io.DatumWriter<UserMovieRatingInfoModule>)MODEL$.createDatumWriter(SCHEMA$);

  @Override public void writeExternal(java.io.ObjectOutput out)
    throws java.io.IOException {
    WRITER$.write(this, SpecificData.getEncoder(out));
  }

  @SuppressWarnings("unchecked")
  private static final org.apache.avro.io.DatumReader<UserMovieRatingInfoModule>
    READER$ = (org.apache.avro.io.DatumReader<UserMovieRatingInfoModule>)MODEL$.createDatumReader(SCHEMA$);

  @Override public void readExternal(java.io.ObjectInput in)
    throws java.io.IOException {
    READER$.read(this, SpecificData.getDecoder(in));
  }

}