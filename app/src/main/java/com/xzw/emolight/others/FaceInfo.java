package com.xzw.emolight.others;

import java.util.List;

public class FaceInfo {

    /**
     * image_id : fI0TwhJ3qwkKtCPxoNDnBg==
     * request_id : 1541100889,9023617c-beb1-43f2-8fb8-6c9aa67d9d1c
     * time_used : 574
     * faces : [{"attributes":{"emotion":{"sadness":2.57,"neutral":97.022,"disgust":0.006,"anger":0.011,"surprise":0.005,"fear":0.005,"happiness":0.381},"gender":{"value":"Male"},"age":{"value":24},"facequality":{"threshold":70.1,"value":81.367},"smile":{"threshold":50,"value":0.511}},"face_rectangle":{"width":676,"top":680,"left":188,"height":676},"face_token":"2a5d141085bda234d233ce8a198ec30e"}]
     */

    private String image_id;
    private String request_id;
    private int time_used;
    private List<FacesBean> faces;

    public String getImage_id() {
        return image_id;
    }

    public void setImage_id(String image_id) {
        this.image_id = image_id;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }

    public int getTime_used() {
        return time_used;
    }

    public void setTime_used(int time_used) {
        this.time_used = time_used;
    }

    public List<FacesBean> getFaces() {
        return faces;
    }

    public void setFaces(List<FacesBean> faces) {
        this.faces = faces;
    }

    public static class FacesBean {
        /**
         * attributes : {"emotion":{"sadness":2.57,"neutral":97.022,"disgust":0.006,"anger":0.011,"surprise":0.005,"fear":0.005,"happiness":0.381},"gender":{"value":"Male"},"age":{"value":24},"facequality":{"threshold":70.1,"value":81.367},"smile":{"threshold":50,"value":0.511}}
         * face_rectangle : {"width":676,"top":680,"left":188,"height":676}
         * face_token : 2a5d141085bda234d233ce8a198ec30e
         */

        private AttributesBean attributes;
        private FaceRectangleBean face_rectangle;
        private String face_token;

        public AttributesBean getAttributes() {
            return attributes;
        }

        public void setAttributes(AttributesBean attributes) {
            this.attributes = attributes;
        }

        public FaceRectangleBean getFace_rectangle() {
            return face_rectangle;
        }

        public void setFace_rectangle(FaceRectangleBean face_rectangle) {
            this.face_rectangle = face_rectangle;
        }

        public String getFace_token() {
            return face_token;
        }

        public void setFace_token(String face_token) {
            this.face_token = face_token;
        }

        public static class AttributesBean {
            /**
             * emotion : {"sadness":2.57,"neutral":97.022,"disgust":0.006,"anger":0.011,"surprise":0.005,"fear":0.005,"happiness":0.381}
             * gender : {"value":"Male"}
             * age : {"value":24}
             * facequality : {"threshold":70.1,"value":81.367}
             * smile : {"threshold":50,"value":0.511}
             */

            private EmotionBean emotion;
            private GenderBean gender;
            private AgeBean age;
            private FacequalityBean facequality;
            private SmileBean smile;

            public EmotionBean getEmotion() {
                return emotion;
            }

            public void setEmotion(EmotionBean emotion) {
                this.emotion = emotion;
            }

            public GenderBean getGender() {
                return gender;
            }

            public void setGender(GenderBean gender) {
                this.gender = gender;
            }

            public AgeBean getAge() {
                return age;
            }

            public void setAge(AgeBean age) {
                this.age = age;
            }

            public FacequalityBean getFacequality() {
                return facequality;
            }

            public void setFacequality(FacequalityBean facequality) {
                this.facequality = facequality;
            }

            public SmileBean getSmile() {
                return smile;
            }

            public void setSmile(SmileBean smile) {
                this.smile = smile;
            }

            public static class EmotionBean {
                /**
                 * sadness : 2.57
                 * neutral : 97.022
                 * disgust : 0.006
                 * anger : 0.011
                 * surprise : 0.005
                 * fear : 0.005
                 * happiness : 0.381
                 */

                private double sadness;
                private double neutral;
                private double disgust;
                private double anger;
                private double surprise;
                private double fear;
                private double happiness;

                public double getSadness() {
                    return sadness;
                }

                public void setSadness(double sadness) {
                    this.sadness = sadness;
                }

                public double getNeutral() {
                    return neutral;
                }

                public void setNeutral(double neutral) {
                    this.neutral = neutral;
                }

                public double getDisgust() {
                    return disgust;
                }

                public void setDisgust(double disgust) {
                    this.disgust = disgust;
                }

                public double getAnger() {
                    return anger;
                }

                public void setAnger(double anger) {
                    this.anger = anger;
                }

                public double getSurprise() {
                    return surprise;
                }

                public void setSurprise(double surprise) {
                    this.surprise = surprise;
                }

                public double getFear() {
                    return fear;
                }

                public void setFear(double fear) {
                    this.fear = fear;
                }

                public double getHappiness() {
                    return happiness;
                }

                public void setHappiness(double happiness) {
                    this.happiness = happiness;
                }
            }

            public static class GenderBean {
                /**
                 * value : Male
                 */

                private String value;

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }
            }

            public static class AgeBean {
                /**
                 * value : 24
                 */

                private int value;

                public int getValue() {
                    return value;
                }

                public void setValue(int value) {
                    this.value = value;
                }
            }

            public static class FacequalityBean {
                /**
                 * threshold : 70.1
                 * value : 81.367
                 */

                private double threshold;
                private double value;

                public double getThreshold() {
                    return threshold;
                }

                public void setThreshold(double threshold) {
                    this.threshold = threshold;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }

            public static class SmileBean {
                /**
                 * threshold : 50
                 * value : 0.511
                 */

                private int threshold;
                private double value;

                public int getThreshold() {
                    return threshold;
                }

                public void setThreshold(int threshold) {
                    this.threshold = threshold;
                }

                public double getValue() {
                    return value;
                }

                public void setValue(double value) {
                    this.value = value;
                }
            }
        }

        public static class FaceRectangleBean {
            /**
             * width : 676
             * top : 680
             * left : 188
             * height : 676
             */

            private int width;
            private int top;
            private int left;
            private int height;

            public int getWidth() {
                return width;
            }

            public void setWidth(int width) {
                this.width = width;
            }

            public int getTop() {
                return top;
            }

            public void setTop(int top) {
                this.top = top;
            }

            public int getLeft() {
                return left;
            }

            public void setLeft(int left) {
                this.left = left;
            }

            public int getHeight() {
                return height;
            }

            public void setHeight(int height) {
                this.height = height;
            }
        }
    }
}
