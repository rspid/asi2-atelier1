import Header from "../components/Header";

const Home = () => {
  return (
    <div className="h-screen bg-black text-white flex flex-col font-body">
      <Header />
      <div className="flex w-full h-full justify-center items-center font-bold">
        Hello to our app
      </div>
    </div>
  );
};
export default Home;
